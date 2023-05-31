package ru.mephi.divkitpages.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import ru.mephi.divkitpages.service.DivKitPageEntity;


@Configuration
public class ReactiveRedisConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "cache")
    public CacheProperties cacheProperties() {
        return new CacheProperties();
    }


    @Bean
    public ReactiveRedisTemplate<String, DivKitPageEntity> reactiveRedisTemplate(
            ReactiveRedisConnectionFactory reactiveRedisConnectionFactory
    ) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<DivKitPageEntity> valueSerializer =
                new Jackson2JsonRedisSerializer<>(DivKitPageEntity.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, DivKitPageEntity> builder =
                RedisSerializationContext.newSerializationContext(keySerializer);
        RedisSerializationContext<String, DivKitPageEntity> context = builder.value(valueSerializer).build();
        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, context);
    }
}