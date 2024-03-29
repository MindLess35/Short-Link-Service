package com.shortlink.webapp.service;

import com.querydsl.core.types.Predicate;
import com.shortlink.webapp.dto.request.LinkCreateDto;
import com.shortlink.webapp.dto.request.LinkUpdateDto;
import com.shortlink.webapp.dto.response.AllLinksReadDto;
import com.shortlink.webapp.dto.response.LinkReadDto;
import com.shortlink.webapp.entity.Link;
import com.shortlink.webapp.entity.User;
import com.shortlink.webapp.event.AddLinkUsageEvent;
import com.shortlink.webapp.exception.InvalidKeyException;
import com.shortlink.webapp.exception.LifeTimeExpiredException;
import com.shortlink.webapp.exception.LinkNotExistsException;
import com.shortlink.webapp.exception.UserNotExistsException;
import com.shortlink.webapp.mapper.LinkCreateDtoMapper;
import com.shortlink.webapp.mapper.LinkReadDtoMapper;
import com.shortlink.webapp.mapper.LinkUpdateDtoMapper;
import com.shortlink.webapp.property.LinkProperty;
import com.shortlink.webapp.repository.LinkRepository;
import com.shortlink.webapp.repository.UserRepository;
import com.shortlink.webapp.util.LinkUtil;
import com.shortlink.webapp.util.QPredicates;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

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
    private final ApplicationEventPublisher publisher;
    private final LinkProperty linkProperty;

    @Cacheable(value = "LinkService::getLinkById", key = "#id") //todo solve n + 1
    public LinkReadDto getLinkById(Long id) {
        return linkRepository.findById(id)
                .map(linkReadDtoMapper::toDto)
                .orElseThrow(() -> new LinkNotExistsException(
                        "Link with id %s does not exists".formatted(id)));
    }

    @Transactional
//    @CacheEvict(value = "LinkService::getLinkById", key = "#") //todo add manual redis cache by deleteAllUsersLinks
    public void deleteAllUsersLinks(Long userId) {
        linkRepository.deleteAllByUser(userRepository.findById(userId)
                .orElseThrow(() -> new UserNotExistsException(
                        "User with id %s does not exists".formatted(userId))));
    }

    @Transactional
    @CachePut(value = "LinkService::getLinkById", key = "#result.id")
    public LinkReadDto createLink(LinkCreateDto dto) {
        setShortLink(dto);
        Link link = getLinkWithUser(dto);

        Link savedLink = linkRepository.save(link);
        linkStatisticsService.createLinkStatistics(savedLink, dto.getTimeToLive());

        String key = dto.getKey();
        if (key != null)
            savedLink.setShortLink(savedLink.getShortLink() + "/" + key);//todo change key accepting

        return linkReadDtoMapper.toDto(savedLink);
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
                    .orElseThrow(() -> new UserNotExistsException(
                            "user with id %s does not exists".formatted(userId)));
        }
        Link link = linkCreateDtoMapper.toEntity(dto);
        if (isLinkWithUser)
            link.setUser(user);
        return link;
    }

    public String getOriginalLinkByKey(String shortLinkName, String maybeKey) {
        Link link = linkRepository.findByShortLink(LinkUtil.APPLICATION_URL + shortLinkName) //todo solve n + 1
                .orElseThrow(() -> new LinkNotExistsException(
                        "link with the name of the short link [%s] does not exists"
                                .formatted(shortLinkName)));

        if (linkStatisticsService.isTimeToLiveExpired(link.getLinkStatistics()))
            throw new LifeTimeExpiredException("the link's lifetime has expired");

        if (LinkUtil.isCorrectKey(maybeKey, link.getKey())) {
            publisher.publishEvent(new AddLinkUsageEvent(link.getLinkStatistics()));
            return link.getOriginalLink();

        } else
            throw new InvalidKeyException(String.format("the %s key is invalid", maybeKey));
    }

    public String getOriginalLink(String shortLinkName) { //todo add manual redis cache by link ttl
        Link link = linkRepository.findByShortLink(LinkUtil.APPLICATION_URL + shortLinkName)
                .orElseThrow(() -> new LinkNotExistsException(
                        "link with the name of the short link [%s] does not exists"
                                .formatted(shortLinkName)));

        if (link.getKey() != null)
            throw new InvalidKeyException("this link is only available by key");

        if (linkStatisticsService.isTimeToLiveExpired(link.getLinkStatistics()))
            throw new LifeTimeExpiredException("the link's lifetime has expired");

        publisher.publishEvent(new AddLinkUsageEvent(link.getLinkStatistics()));
        return link.getOriginalLink();
    }

    @Transactional
    @CacheEvict(value = "LinkService::getLinkById", key = "#id")
    public void deleteLink(Long id) {
        linkRepository.delete(linkRepository.findById(id)
                .orElseThrow(() -> new LinkNotExistsException(
                        "link with id %s does not exists".formatted(id))));
    }


    @Transactional
    @CachePut(value = "LinkService::getLinkById", key = "#id")
    public LinkReadDto changeLink(Long id, Map<String, Object> fields) {
        Optional<Link> link = linkRepository.findById(id);

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
            throw new LinkNotExistsException("link with id [%s] does not exists".formatted(id));
    }

    @Transactional
    @CachePut(value = "LinkService::getLinkById", key = "#id")
    public LinkReadDto updateLink(Long id, LinkUpdateDto dto) {
        return linkRepository.findById(id)
                .map(link -> linkUpdateDtoMapper.updateEntity(link, dto))
                .map(linkRepository::save)
                .map(linkReadDtoMapper::toDto)
                .orElseThrow(() -> new LinkNotExistsException(
                        "Link with id %s does not exists".formatted(id)));
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
                .orElseThrow(() -> new UserNotExistsException(
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

