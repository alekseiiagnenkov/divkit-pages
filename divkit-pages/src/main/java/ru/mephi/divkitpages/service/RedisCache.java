package ru.mephi.divkitpages.service;

import java.time.Duration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.mephi.divkitpages.config.CacheProperties;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisCache {

    private final CacheProperties cacheProperties;

    private final ReactiveRedisTemplate<String, DivKitPageEntity> reactiveRedisTemplate;

    public Mono<DivKitPageEntity> getById(String screenId) {
        return reactiveRedisTemplate.opsForValue().get(screenId)
                .onErrorResume(e -> {
                    log.error("Error requesting divkit page from cache", e);
                    return Mono.empty();
                });
    }

    public Mono<DivKitPageEntity> save(DivKitPageEntity model) {
        return reactiveRedisTemplate.opsForValue().set(
                        model.getPageId(),
                        model,
                        Duration.ofSeconds(cacheProperties.getTimeToLiveInSeconds())
                )
                .map(bool -> model)
                .onErrorResume(e -> {
                    log.error("Error saving divkit page to cache", e);
                    return Mono.just(model);
                });
    }
}