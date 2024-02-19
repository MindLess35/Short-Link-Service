package com.shortlink.webapp.service;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.shortlink.webapp.dto.request.FilterLink;
import com.shortlink.webapp.dto.request.LinkCreateEditDto;
import com.shortlink.webapp.dto.response.LinkReadDto;
import com.shortlink.webapp.entity.Link;
import com.shortlink.webapp.entity.LinkStatistics;
import com.shortlink.webapp.entity.User;
import com.shortlink.webapp.event.AddLinkUsageEvent;
import com.shortlink.webapp.exception.InvalidKeyException;
import com.shortlink.webapp.exception.LifeTimeExpiredException;
import com.shortlink.webapp.exception.LinkNotExistsException;
import com.shortlink.webapp.exception.UserNotExistsException;
import com.shortlink.webapp.mapper.LinkCreateEditMapper;
import com.shortlink.webapp.mapper.LinkReadMapper;
import com.shortlink.webapp.querydsl.QPredicates;
import com.shortlink.webapp.repository.LinkRepository;
import com.shortlink.webapp.repository.LinkStatisticsRepository;
import com.shortlink.webapp.repository.UserRepository;
import com.shortlink.webapp.util.LinkUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.shortlink.webapp.entity.QLink.link;


@Service
@Setter
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LinkService {

    private final LinkRepository linkRepository;
    private final LinkStatisticsRepository linkStatisticsRepository;
    private final UserRepository userRepository;
    private final LinkCreateEditMapper linkCreateEditMapper;
    private final LinkReadMapper linkReadMapper;
    private final LinkStatisticsService linkStatisticsService;
    private final LinkUtil linkUtil;
    //    private final QPredicates qPredicates;
    private final ApplicationEventPublisher publisher;

    //    public class LinkCreateDto {
//        private Long userId;  +
//        private Long lifeTime; +
//        private String originalLink; +
//        private String key; +
//        private String customLinkName; +
//    }
    @Transactional
    public LinkReadDto createLink(LinkCreateEditDto dto) {
        String customLinkName = dto.getCustomLinkName();
        if (customLinkName == null)
            do {
                customLinkName = linkUtil.generateRandomString();
            } while (linkRepository.shortLinkNameAlreadyExists(customLinkName));
        dto.setCustomLinkName(customLinkName);

        String key = dto.getKey();
        if (key != null)
            dto.setKey(linkUtil.encryptKeySHA256(key));

        User user = null;
        if (dto.getUserId() != null) {
            user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new UserNotExistsException(
                            "user with id [%s] does not exists".formatted(dto.getUserId())));

        }
        Link link = linkCreateEditMapper.toEntity(dto);
        if (dto.getUserId() != null)
            link.setUser(user);

        Link savedLink = linkRepository.save(link);

        Long lifeTimeInSeconds = dto.getLifeTime();
        if (lifeTimeInSeconds == null)
            lifeTimeInSeconds = Long.MAX_VALUE;

        linkStatisticsService.createLinkStatistics(savedLink, lifeTimeInSeconds);

        if (key != null)
            savedLink.setShortLink(savedLink.getShortLink() + "/" + key);

        return linkReadMapper.toDto(savedLink);
    }

    public String getOriginalLinkByKey(String shortLinkName, String key) {
        Link link = linkRepository.findByShortLink(shortLinkName)
                .orElseThrow(() -> new LinkNotExistsException(
                        "link with the name of the short link [%s] does not exists"
                                .formatted(shortLinkName)));

        if (linkStatisticsService.iskLifeTimeExpired(link.getLinkStatistics()))
            throw new LifeTimeExpiredException("the link's lifetime has expired");

        if (linkUtil.isCorrectKey(key, link.getEncryptedKey())) {
//            linkStatisticsService.addUsage(link);
            publisher.publishEvent(new AddLinkUsageEvent(link.getLinkStatistics()));
            return link.getOriginalLink();

        } else
            throw new InvalidKeyException(String.format("the '%s' key is invalid", key));
    }

    //    @Transactional
    public String getOriginalLink(String shortLinkName) {
        Link link = linkRepository.findByShortLink(shortLinkName)
                .orElseThrow(() -> new LinkNotExistsException(
                        "link with the name of the short link [%s] does not exists"
                                .formatted(shortLinkName)));

        if (link.getEncryptedKey() != null)
            throw new InvalidKeyException("this link is only available by key");

        if (linkStatisticsService.iskLifeTimeExpired(link.getLinkStatistics()))
            throw new LifeTimeExpiredException("the link's lifetime has expired");

//        linkStatisticsService.addUsage(link);
        publisher.publishEvent(new AddLinkUsageEvent(link.getLinkStatistics()));
        return link.getOriginalLink();
    }

    public List<LinkReadDto> getAllLinks() {
        return linkRepository.findAll()
                .stream()
                .map(linkReadMapper::toDto)
                .toList();
    }

    public List<LinkReadDto> getAllUsersLinks(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotExistsException(
                "user with id [%s] does not exists".formatted(id)));

        return linkRepository.findAllByUser(user)
                .stream()
                .map(linkReadMapper::toDto)
                .toList();
    }

    public List<LinkReadDto> getAllLinksWithoutUser() {
        return linkRepository.findAllByUserIsNull()
                .stream()
                .map(linkReadMapper::toDto)
                .toList();
    }

    public List<LinkReadDto> getAllLinksWithUser() {
        return linkRepository.findAllByUserIsNotNull()
                .stream()
                .map(linkReadMapper::toDto)
                .toList();
    }

    @Transactional
    public void deleteLink(Long id) {
        linkRepository.delete(linkRepository.findById(id)
                .orElseThrow(() -> new LinkNotExistsException(
                        "link with id [%s] does not exists".formatted(id))));
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
                field.setAccessible(true);
                ReflectionUtils.setField(field, link.get(), value);
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

    public Page<Link> findAllByPageableAndFilter(Pageable pageable, FilterLink filter) {
        Predicate predicate = QPredicates.builder()
                .add(filter.getOriginalLink(), link.originalLink::containsIgnoreCase)
                .add(filter.getShortLink(), str -> link.linkStatistics.lifeTime.eq(1L))
                .add(filter.getUserExists(), isExists -> {
                    if (isExists)
                        return link.user.isNotNull();
                    return link.user.isNull();
                })
                .buildAnd();
        System.out.println();
        Page<Link> all = linkRepository.findAll(predicate, pageable);
        System.out.println();
        return all;
//                .stream()
//                .map(linkReadMapper::toDto)
//                ;
    }


//    link_id             BIGINT      NOT NULL REFERENCES link(id),
//    date_of_creation    TIMESTAMP   NOT NULL,
//    date_of_last_uses   TIMESTAMP   NOT NULL,
//    life_time           BIGINT      NOT NULL,
//    count_of_uses       BIGINT      NOT NULL
}

