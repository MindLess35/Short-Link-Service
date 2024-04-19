package com.shortlink.webapp.service;

import com.shortlink.webapp.dto.response.RedirectLinkDto;
import com.shortlink.webapp.entity.Link;
import com.shortlink.webapp.entity.LinkStatistics;
import com.shortlink.webapp.event.AddLinkUsageEvent;
import com.shortlink.webapp.exception.link.InvalidKeyException;
import com.shortlink.webapp.exception.base.ResourceNotFoundException;
import com.shortlink.webapp.exception.link.TimeToLiveExpiredException;
import com.shortlink.webapp.mapper.RedirectLinkDtoMapper;
import com.shortlink.webapp.property.CacheProperty;
import com.shortlink.webapp.repository.LinkRepository;
import com.shortlink.webapp.util.LinkUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LinkRedirectService {
    private final LinkRepository linkRepository;
    private final LinkStatisticsService linkStatisticsService;
    private final ApplicationEventPublisher publisher;
    private final CacheProperty cacheProperty;
    private final RedisTemplate<String, RedirectLinkDto> redisTemplate;
    private final RedirectLinkDtoMapper redirectLinkDtoMapper;

    public RedirectLinkDto getOriginalLink(String shortLinkName) { //todo add manual redis cache by link ttl
        RedirectLinkDto linkFromCache = getLinkFromCache(shortLinkName);
        String shortLink = LinkUtil.APPLICATION_URL + shortLinkName;
        if (linkFromCache != null) {
            incrementLinkUsageAsync(shortLink);
            return linkFromCache;
        }

        Link link = linkRepository.findByShortLink(shortLink)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Link with the short link %s does not exists"
                                .formatted(shortLink)));

        LinkStatistics linkStatistics = link.getLinkStatistics();
        RedirectLinkDto redirectLinkDto = redirectLinkDtoMapper.toDto(link);

        Instant timeToLive = linkStatistics.getTimeToLive();
        if (redirectLinkDto.isKeyExists()) {
            cacheLink(redirectLinkDto, timeToLive, shortLinkName);
            return redirectLinkDto;
        }

        incrementLinkUsage(linkStatistics);
        cacheLink(redirectLinkDto, timeToLive, shortLinkName);
        return redirectLinkDto;
    }

    @Async
    protected void incrementLinkUsageAsync(String shortLink) {
        Link link = linkRepository.findByShortLink(shortLink)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Link with the short link %s does not exists"
                                .formatted(shortLink)));

        LinkStatistics linkStatistics = link.getLinkStatistics();
        incrementLinkUsage(linkStatistics);
    }

    public RedirectLinkDto checkLinkKey(String shortLinkName, String key) {
        RedirectLinkDto linkFromCache = getLinkFromCache(shortLinkName);
        String shortLink = LinkUtil.APPLICATION_URL + shortLinkName;
        if (linkFromCache != null && linkFromCache.isKeyCorrect(key)) {
            incrementLinkUsageAsync(shortLink);
            return linkFromCache;
        }

        Link link = linkRepository.findByShortLink(shortLink) // todo solve n + 1
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Link with the short link %s does not exists"
                                .formatted(shortLink)));

        RedirectLinkDto redirectLinkDto = redirectLinkDtoMapper.toDto(link);
        if (!redirectLinkDto.isKeyCorrect(key))
            throw new InvalidKeyException("This link is only available by correct key");

        LinkStatistics linkStatistics = link.getLinkStatistics();
        incrementLinkUsage(linkStatistics);
        cacheLink(redirectLinkDto, linkStatistics.getTimeToLive(), shortLinkName);
        return redirectLinkDtoMapper.toDto(link);
    }

    private void incrementLinkUsage(LinkStatistics linkStatistics) {
        if (linkStatisticsService.isTimeToLiveExpired(linkStatistics))
            throw new TimeToLiveExpiredException("The link's lifetime has expired");

        publisher.publishEvent(new AddLinkUsageEvent(linkStatistics));
    }

    private void cacheLink(RedirectLinkDto redirectLinkDto, Instant ttl, String shortLinkName) {
        redisTemplate.opsForValue().set(getLinkKey(shortLinkName),
                redirectLinkDto, resolveTtl(ttl), TimeUnit.SECONDS);
    }

    private RedirectLinkDto getLinkFromCache(String shortLinkName) {
        return redisTemplate.opsForValue().get(getLinkKey(shortLinkName));
    }

    private String getLinkKey(String shortLinkName) {
        return cacheProperty.getRedirectCacheName() + "::" + shortLinkName;
    }

    private long resolveTtl(Instant ttl) {
        long linkCacheTtlInSeconds = cacheProperty.getRedirectCacheTtlInSeconds();
        if (ttl == null)
            return linkCacheTtlInSeconds;

        long userTtlInSecond = ttl
                .minus(Instant.now().getEpochSecond(), ChronoUnit.SECONDS).getEpochSecond();
        return Math.min(userTtlInSecond, linkCacheTtlInSeconds);
    }

    public ResponseEntity<?> buildResponseEntity(String originalLink) {
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, originalLink)
                .build();
    }
}
