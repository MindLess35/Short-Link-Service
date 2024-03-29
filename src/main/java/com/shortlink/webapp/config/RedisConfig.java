package com.shortlink.webapp.config;

import com.shortlink.webapp.property.MailProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@EnableCaching
@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final MailProperty mailProperty;


//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
//        lettuceConnectionFactory.setHostName("localhost");
//        lettuceConnectionFactory.setPort(6379);
//        lettuceConnectionFactory.afterPropertiesSet();
//        return lettuceConnectionFactory;
//    }

//    @Bean
//    public RedisTemplate<String, Object> redisTemplate() {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory());
//        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate.opsForValue();
//        return redisTemplate.opsForHash().put();
//        return redisTemplate.opsForSet().;
//        return redisTemplate.execute();
//        return redisTemplate.execute();
//        return redisTemplate.expire();
//        return redisTemplate.boundValueOps().;
//        return redisTemplate.executePipelined();
//        new HashSet<>().
//        return redisTemplate.opsForZSet().;
//    }

//    @Bean
//    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
//        return (builder) -> builder
//                .withCacheConfiguration("itemCache",
//                        RedisCacheConfiguration
//                                .defaultCacheConfig()
//                                .entryTtl(Duration.ofMinutes(10)))
//
//                .withCacheConfiguration("customerCache",
//                        RedisCacheConfiguration
//                                .defaultCacheConfig()
//                                .entryTtl(Duration.ofMinutes(5)));
//    }
//
//    @Bean
//    public RedisCacheConfiguration cacheConfiguration() {
//        return RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(60))
//                .disableCachingNullValues()
//                .serializeValuesWith(RedisSerializationContext.SerializationPair
//                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));
//    }
//
//    @Bean
//    public RedisCacheConfiguration cache1Configuration() {
//        return RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(5));
//    }
//
//    @Bean
//    public RedisCacheConfiguration cache2Configuration() {
//        return RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofHours(1));
//    }
//
//    @Bean
//    public CacheManager cacheManager1(RedisConnectionFactory redisConnectionFactory) {
//        return RedisCacheManager.builder(redisConnectionFactory)
//                .cacheDefaults(cache1Configuration())
//                .build();
//    }

//    @Bean
//    public CacheManager cacheManager2(RedisConnectionFactory redisConnectionFactory) {
//        //new RedisCacheManager().getCache().evict();
//        return RedisCacheManager.builder(redisConnectionFactory)
//                .cacheDefaults(cache2Configuration())
//                .build();
//    }
//    @Cacheable(value = "cache1", cacheManager = "cacheManager1")
//    public String myCachedMethod1() {
//        // Ваш код
//        return "Cached Data 1";
//    }
//
//    @Cacheable(value = "cache2", cacheManager = "cacheManager2")
//    public String myCachedMethod2() {
//        // Ваш код
//        return "Cached Data 2";
//    }


}
