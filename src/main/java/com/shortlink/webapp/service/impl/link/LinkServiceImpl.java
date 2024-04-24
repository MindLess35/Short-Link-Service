package com.shortlink.webapp.service.impl.link;

import com.querydsl.core.types.Predicate;
import com.shortlink.webapp.domain.entity.link.Link;
import com.shortlink.webapp.domain.entity.user.User;
import com.shortlink.webapp.domain.exception.base.ResourceNotFoundException;
import com.shortlink.webapp.domain.property.CacheProperty;
import com.shortlink.webapp.domain.property.LinkProperty;
import com.shortlink.webapp.dto.link.request.AllLinksReadDto;
import com.shortlink.webapp.dto.link.request.LinkCreateDto;
import com.shortlink.webapp.dto.link.request.LinkUpdateDto;
import com.shortlink.webapp.dto.link.response.LinkReadDto;
import com.shortlink.webapp.dto.projection.link.LinkWithTtlProjection;
import com.shortlink.webapp.mapper.link.LinkCreateDtoMapper;
import com.shortlink.webapp.mapper.link.LinkReadDtoMapper;
import com.shortlink.webapp.mapper.link.LinkUpdateDtoMapper;
import com.shortlink.webapp.repository.jpa.link.LinkRepository;
import com.shortlink.webapp.repository.jpa.user.UserRepository;
import com.shortlink.webapp.service.interfaces.link.LinkService;
import com.shortlink.webapp.service.interfaces.link.LinkStatisticsService;
import com.shortlink.webapp.util.LinkUtil;
import com.shortlink.webapp.util.QPredicates;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static com.shortlink.webapp.domain.entity.link.QLink.link;
import static com.shortlink.webapp.domain.entity.link.QLinkStatistics.linkStatistics;
import static com.shortlink.webapp.domain.entity.user.QUser.user;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LinkServiceImpl implements LinkService {
    private final LinkRepository linkRepository;
    private final UserRepository userRepository;
    private final LinkCreateDtoMapper linkCreateDtoMapper;
    private final LinkUpdateDtoMapper linkUpdateDtoMapper;
    private final LinkReadDtoMapper linkReadDtoMapper;
    private final LinkStatisticsService linkStatisticsService;
    private final LinkProperty linkProperty;
    private final RedisTemplate<String, LinkReadDto> redisTemplate;
    private final CacheProperty cacheProperty;

    @Override
    public LinkReadDto getLinkById(Long id) {
        LinkReadDto result = getLinkFromCache(id);
        if (result != null)
            return result;

        //todo solve n + 1 when find link also retrieved link stat
        LinkWithTtlProjection linkWithTtl = linkRepository.findLinkWithTtlById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Link with id %s does not exists".formatted(id)));
        LinkReadDto linkReadDto = linkReadDtoMapper.toDto(linkWithTtl);

        cacheLink(linkReadDto, linkWithTtl.getTtl());
        return linkReadDto;
    }

    @Override
    @Transactional
    public LinkReadDto createLink(LinkCreateDto dto) {
        setShortLink(dto);
        Link link = getLinkWithUser(dto);

        Instant timeToLive = dto.getTimeToLive();
        Link savedLink = linkRepository.save(link);
        linkStatisticsService.createLinkStatistics(savedLink, timeToLive);

        LinkReadDto linkReadDto = linkReadDtoMapper.toDto(savedLink);

        cacheLink(linkReadDto, timeToLive);

        return linkReadDto;
    }

    private void setShortLink(LinkCreateDto dto) {
        String shortLinkName = dto.getCustomLinkName();
        String applicationUrl = LinkUtil.APPLICATION_URL;
        String shortLink = applicationUrl + shortLinkName;
        Short count = linkProperty.getCount();

        if (shortLinkName == null) {
            do {
                shortLinkName = LinkUtil.generateRandomString(count);
                shortLink = applicationUrl + shortLinkName;
            } while (linkRepository.existsByShortLink(shortLink));
        }
        dto.setCustomLinkName(shortLink);//TODO
    }

    private Link getLinkWithUser(LinkCreateDto dto) {
        User user = null;
        Long userId = dto.getUserId();
        boolean isLinkWithUser = userId != null;

        if (isLinkWithUser) {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "user with id %s does not exists".formatted(userId)));
        }
        Link link = linkCreateDtoMapper.toEntity(dto);
        if (isLinkWithUser)
            link.setUser(user);
        return link;
    }

    private long resolveTtl(Instant ttl) {
        long linkCacheTtlInSeconds = cacheProperty.getLinkCacheTtlInSeconds();
        if (ttl == null)
            return linkCacheTtlInSeconds;

        long userTtlInSecond = ttl
                .minus(Instant.now().getEpochSecond(), ChronoUnit.SECONDS).getEpochSecond();
        return Math.min(userTtlInSecond, linkCacheTtlInSeconds);
    }

    private void cacheLink(LinkReadDto linkReadDto, Instant ttl) {
        redisTemplate.opsForValue().set(getLinkKey(linkReadDto.getId()),
                linkReadDto, resolveTtl(ttl), TimeUnit.SECONDS);
    }

    private LinkReadDto getLinkFromCache(Long key) {
        return redisTemplate.opsForValue().get(getLinkKey(key));
    }

    private String getLinkKey(Long key) {
        return cacheProperty.getLinkCacheName() + "::" + key;
    }

    @Override
    @Transactional
    public void deleteAllUsersLinks(Long userId) {
        Stream<Long> linkIds = linkRepository.deleteAllByUserId(userRepository.findById(userId)
                .map(User::getId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id %s does not exists".formatted(userId))));

        redisTemplate.delete(linkIds
                .map(this::getLinkKey)
                .toList()
        );
    }

    @Override
    @Transactional
    public void deleteLink(Long id) {
        linkRepository.delete(linkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "link with id %s does not exists".formatted(id))));

        redisTemplate.delete(getLinkKey(id));
    }

    @Override
    @Transactional
    public LinkReadDto changeLink(Long id, Map<String, Object> fields) {
        Optional<Link> link = linkRepository.findById(id);//TODO do anything with this patch change

        if (link.isPresent()) {
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(User.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, link.get(), value);
                }
            });
            return linkReadDtoMapper.toDto(linkRepository.save(link.get()));
        } else
            throw new ResourceNotFoundException("link with id %s does not exists".formatted(id));
    }

    @Override
    @Transactional
    public LinkReadDto updateLink(Long id, LinkUpdateDto dto) {
        LinkWithTtlProjection linkWithTtl = linkRepository.findLinkWithTtlById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Link with id %s does not exists".formatted(id)));

        dto.setCustomLinkName(LinkUtil.APPLICATION_URL + dto.getCustomLinkName());

        LinkReadDto linkReadDto = Optional.of(dto)
                .map(d -> linkUpdateDtoMapper.updateEntity(id, d))
                .map(linkRepository::save)// todo n + 1 solve
                .map(linkReadDtoMapper::toDto)
                .get();

        linkStatisticsService.changeTtl(id, dto.getTimeToLive());

        cacheLink(linkReadDto, linkWithTtl.getTtl());
        return linkReadDto;
    }

    @Override
    public Page<AllLinksReadDto> findAllLinksByPageableAndFilter(Pageable pageable,
                                                                 String shortLink,
                                                                 String originalLink,
                                                                 Instant dateOfCreation,
                                                                 Instant dateOfLastUses,
                                                                 Long countOfUses,
                                                                 Boolean isUserExists,
                                                                 Instant timeToLive) {
        QPredicates qPredicate = createQPredicate(
                shortLink,
                originalLink,
                dateOfCreation,
                dateOfLastUses,
                countOfUses,
                timeToLive);

        Predicate predicate = qPredicate.add(isUserExists, isExists ->
                isExists ? link.user.isNotNull() : link.user.isNull()).buildAnd();

        return linkRepository.findPageOfLinks(predicate, pageable);
    }

    private QPredicates createQPredicate(String shortLink,
                                         String originalLink,
                                         Instant dateOfCreation,
                                         Instant dateOfLastUses,
                                         Long countOfUses,
                                         Instant timeToLive
    ) {
        return QPredicates.builder()
                .add(originalLink, link.originalLink::containsIgnoreCase)
                .add(shortLink, link.shortLink::containsIgnoreCase)
                .add(dateOfCreation, linkStatistics.dateOfCreation::before)
                .add(dateOfLastUses, linkStatistics.dateOfLastUses::before)
                .add(countOfUses, linkStatistics.countOfUses::goe)
                .add(timeToLive, linkStatistics.timeToLive::before);
    }

    @Override
    public Page<AllLinksReadDto> getAllUsersLinksByPageableAndFilter(Long id,
                                                                     Pageable pageable,
                                                                     String shortLink,
                                                                     String originalLink,
                                                                     Instant dateOfCreation,
                                                                     Instant dateOfLastUses,
                                                                     Long countOfUses,
                                                                     Instant timeToLive) {
        User retrievedUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id %s does not exists".formatted(id)));

        QPredicates qPredicate = createQPredicate(
                shortLink,
                originalLink,
                dateOfCreation,
                dateOfLastUses,
                countOfUses,
                timeToLive);

        Predicate predicate = qPredicate.add(retrievedUser, u -> user.id.eq(u.getId()))
                .buildAnd();

        return linkRepository.findPageOfLinksForUser(predicate, pageable);
    }

}

