package com.shortlink.webapp.service;

import com.querydsl.core.types.Predicate;
import com.shortlink.webapp.dto.projection.LinkWithTtlProjection;
import com.shortlink.webapp.dto.request.LinkCreateDto;
import com.shortlink.webapp.dto.request.LinkUpdateDto;
import com.shortlink.webapp.dto.response.AllLinksReadDto;
import com.shortlink.webapp.dto.response.LinkReadDto;
import com.shortlink.webapp.entity.Link;
import com.shortlink.webapp.entity.User;
import com.shortlink.webapp.exception.base.ResourceNotFoundException;
import com.shortlink.webapp.mapper.LinkCreateDtoMapper;
import com.shortlink.webapp.mapper.LinkReadDtoMapper;
import com.shortlink.webapp.mapper.LinkUpdateDtoMapper;
import com.shortlink.webapp.property.CacheProperty;
import com.shortlink.webapp.property.LinkProperty;
import com.shortlink.webapp.repository.LinkRepository;
import com.shortlink.webapp.repository.UserRepository;
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

import static com.shortlink.webapp.entity.QLink.link;
import static com.shortlink.webapp.entity.QLinkStatistics.linkStatistics;
import static com.shortlink.webapp.entity.QUser.user;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LinkService {
    private final LinkRepository linkRepository;
    private final UserRepository userRepository;
    private final LinkCreateDtoMapper linkCreateDtoMapper;
    private final LinkUpdateDtoMapper linkUpdateDtoMapper;
    private final LinkReadDtoMapper linkReadDtoMapper;
    private final LinkStatisticsService linkStatisticsService;
    private final LinkProperty linkProperty;
    private final RedisTemplate<String, LinkReadDto> redisTemplate;
    private final CacheProperty cacheProperty;

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

//    private LinkReadDto setUserIdToDto(Link link) {
//        Long userId = null;
//        User user = link.getUser();
//        if (user != null)
//            userId = user.getId();
//
//        LinkReadDto linkReadDto = linkReadDtoMapper.toDto(link);
//        linkReadDto.setUserId(userId);
//        return linkReadDto;
//    }

    @Transactional
    public void deleteLink(Long id) {
        linkRepository.delete(linkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "link with id %s does not exists".formatted(id))));

        redisTemplate.delete(getLinkKey(id));
    }


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

//    @Transactional
//    public LinkReadDto updateLink(Long id, LinkUpdateDto dto) {
//        LinkReadDto linkReadDto = linkRepository.findById(id)
//                .map(link -> linkUpdateDtoMapper.updateEntity(link, dto))
//                .map(linkRepository::save)
//                .map(linkReadDtoMapper::toDto)
//                .orElseThrow(() -> new ResourceNotFoundException(
//                        "Link with id %s does not exists".formatted(id)));
//        return linkReadDto;
//    }

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

