package com.shortlink.webapp.service;

import com.querydsl.core.types.Predicate;
import com.shortlink.webapp.dto.request.LinkCreateEditDto;
import com.shortlink.webapp.dto.response.AllLinksReadDto;
import com.shortlink.webapp.dto.response.LinkReadDto;
import com.shortlink.webapp.entity.Link;
import com.shortlink.webapp.entity.User;
import com.shortlink.webapp.event.AddLinkUsageEvent;
import com.shortlink.webapp.exception.InvalidKeyException;
import com.shortlink.webapp.exception.LifeTimeExpiredException;
import com.shortlink.webapp.exception.LinkNotExistsException;
import com.shortlink.webapp.exception.UserNotExistsException;
import com.shortlink.webapp.mapper.LinkCreateEditMapper;
import com.shortlink.webapp.mapper.LinkReadMapper;
import com.shortlink.webapp.property.LinkProperty;
import com.shortlink.webapp.repository.LinkRepository;
import com.shortlink.webapp.repository.UserRepository;
import com.shortlink.webapp.util.LinkUtil;
import com.shortlink.webapp.util.QPredicates;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static com.shortlink.webapp.entity.QLink.link;
import static com.shortlink.webapp.entity.QLinkStatistics.linkStatistics;
import static com.shortlink.webapp.entity.QUser.user;

//@Setter
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LinkService {

    private final LinkRepository linkRepository;
    private final UserRepository userRepository;
    private final LinkCreateEditMapper linkCreateEditMapper;
    private final LinkReadMapper linkReadMapper;
    private final LinkStatisticsService linkStatisticsService;
    private final ApplicationEventPublisher publisher;
    private final LinkProperty linkProperty;

    @Transactional
    public LinkReadDto createLink(LinkCreateEditDto dto) {
        setShortLink(dto);
        Link link = getLinkWithUser(dto);

        Link savedLink = linkRepository.save(link);

        Long lifeTime = dto.getLifeTime();
        if (lifeTime == null)
            lifeTime = Long.MAX_VALUE;

        linkStatisticsService.createLinkStatistics(savedLink, lifeTime);

        String key = dto.getKey();
        if (key != null)
            savedLink.setShortLink(savedLink.getShortLink() + "/" + key);

        return linkReadMapper.toDto(savedLink);
    }

    private void setShortLink(LinkCreateEditDto dto) {
        String shortLinkName = dto.getCustomLinkName();
        String uri = LinkUtil.APPLICATION_URL;
        String shortLink = uri + shortLinkName;

        if (shortLinkName == null) {
            do {
                shortLinkName = LinkUtil.generateRandomString(linkProperty.getCount());
                shortLink = uri + shortLinkName;
            } while (linkRepository.existsByShortLink(shortLink));
        }
        dto.setCustomLinkName(shortLink);//TODO
    }

    private Link getLinkWithUser(LinkCreateEditDto dto) {
        User user = null;
        Long userId = dto.getUserId();
        if (userId != null) {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotExistsException(
                            "user with id [%s] does not exists".formatted(userId)));

        }
        Link link = linkCreateEditMapper.toEntity(dto);
//        if (userId != null)
        link.setUser(user);
        return link;
    }

    public String getOriginalLinkByKey(String shortLinkName, String maybeKey) {
        Link link = linkRepository.findByShortLink(LinkUtil.APPLICATION_URL + shortLinkName)
                .orElseThrow(() -> new LinkNotExistsException(
                        "link with the name of the short link [%s] does not exists"
                                .formatted(shortLinkName)));

        if (linkStatisticsService.isLifeTimeExpired(link.getLinkStatistics()))
            throw new LifeTimeExpiredException("the link's lifetime has expired");

        if (LinkUtil.isCorrectKey(maybeKey, link.getKey())) {
            publisher.publishEvent(new AddLinkUsageEvent(link.getLinkStatistics()));
            return link.getOriginalLink();

        } else
            throw new InvalidKeyException(String.format("the %s key is invalid", maybeKey));
    }

    //    @Transactional
    public String getOriginalLink(String shortLinkName) {
        Link link = linkRepository.findByShortLink(LinkUtil.APPLICATION_URL + shortLinkName)
                .orElseThrow(() -> new LinkNotExistsException(
                        "link with the name of the short link [%s] does not exists"
                                .formatted(shortLinkName)));

        if (link.getKey() != null)
            throw new InvalidKeyException("this link is only available by key");

        if (linkStatisticsService.isLifeTimeExpired(link.getLinkStatistics()))
            throw new LifeTimeExpiredException("the link's lifetime has expired");

//        linkStatisticsService.addUsage(link);
        publisher.publishEvent(new AddLinkUsageEvent(link.getLinkStatistics()));
        return link.getOriginalLink();
    }

//    public List<LinkReadDto> getAllLinks() {
//        return linkRepository.findAll()
//                .stream()
//                .map(linkReadMapper::toDto)
//                .toList();
//    }

    @Transactional
    public void deleteLink(Long id) {
        linkRepository.delete(linkRepository.findById(id)
                .orElseThrow(() -> new LinkNotExistsException(
                        "link with id %s does not exists".formatted(id))));
    }


    @Transactional
    public void deleteAllUsersLinks(Long userId) {
        linkRepository.deleteAllByUser(userRepository.findById(userId)
                .orElseThrow(() -> new UserNotExistsException(
                        "user with id [%s] does not exists".formatted(userId))));
    }

    @Transactional
    public LinkReadDto changeLink(Long id, Map<String, Object> fields) {
        Optional<Link> link = linkRepository.findById(id);

        if (link.isPresent()) {
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(User.class, key);
                if (field != null) {
                    field.setAccessible(true);
                }
                if (field != null) {
                    ReflectionUtils.setField(field, link.get(), value);
                }
            });
            return linkReadMapper.toDto(linkRepository.save(link.get()));
        } else
            throw new LinkNotExistsException("link with id [%s] does not exists".formatted(id));
    }

    @Transactional
    public LinkReadDto updateLink(Long id, LinkCreateEditDto dto) {
        return linkRepository.findById(id)
                .map(link -> linkCreateEditMapper.updateEntity(link, dto))
                .map(linkRepository::saveAndFlush)
                .map(linkReadMapper::toDto)
                .orElseThrow(() -> new LinkNotExistsException(
                        "link with id [%s] does not exists".formatted(id)));

    }

    public Page<AllLinksReadDto> findAllLinksByPageableAndFilter(Pageable pageable,
                                                                 String shortLink,
                                                                 String originalLink,
                                                                 LocalDateTime dateOfCreation,
                                                                 LocalDateTime dateOfLastUses,
                                                                 Long countOfUses,
                                                                 Boolean isUserExists) {

        QPredicates qPredicate = createQPredicate(
                shortLink,
                originalLink,
                dateOfCreation,
                dateOfLastUses,
                countOfUses);

        Predicate predicate = qPredicate.add(isUserExists, isExists ->
                        isExists ? link.user.isNotNull() : link.user.isNull()).buildAnd();

        return linkRepository.findPageOfLinks(predicate, pageable);
    }

    private QPredicates createQPredicate(String shortLink,
                                         String originalLink,
                                         LocalDateTime dateOfCreation,
                                         LocalDateTime dateOfLastUses,
                                         Long countOfUses
    ) {
        return QPredicates.builder()
                .add(originalLink, link.originalLink::containsIgnoreCase)
                .add(shortLink, link.shortLink::containsIgnoreCase)
                .add(dateOfCreation, linkStatistics.dateOfCreation::before)
                .add(dateOfLastUses, linkStatistics.dateOfLastUses::before)
                .add(countOfUses, linkStatistics.countOfUses::goe);
    }

    public Page<AllLinksReadDto> getAllUsersLinksByPageableAndFilter(Long id,
                                                                     Pageable pageable,
                                                                     String shortLink,
                                                                     String originalLink,
                                                                     LocalDateTime dateOfCreation,
                                                                     LocalDateTime dateOfLastUses,
                                                                     Long countOfUses) {
        User retrievedUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotExistsException(
                        "user with id [%s] does not exists".formatted(id)));

        QPredicates qPredicate = createQPredicate(
                shortLink,
                originalLink,
                dateOfCreation,
                dateOfLastUses,
                countOfUses);

        Predicate predicate = qPredicate.add(retrievedUser, u -> user.id.eq(u.getId()))
                .buildAnd();

        return linkRepository.findPageOfLinksForUser(predicate, pageable);
    }

    public LinkReadDto getLinkById(Long id) {
        return linkRepository.findById(id)
                .map(linkReadMapper::toDto)
                .orElseThrow(() -> new LinkNotExistsException(
                        "link with id [%s] does not exists".formatted(id)));
    }


}

