package ru.mephi.divkitpages.service;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.mephi.divkitpages.repository.DivScreenRepository;
import ru.mephi.divkitpages.service.exceptions.DivKitNotFoundException;
import ru.mephi.divkitpages.service.exceptions.DivKitExistsException;
import ru.mephi.divkitpages.service.exceptions.FieldValidationException;

@Service
@Slf4j
@RequiredArgsConstructor
public class DivKitService {
    private static final String VALIDATION_REGEX = "^[a-zA-Z0-9_-]+$";
    private final ObjectMapper jsonMapper;

    private final RedisCache redisCache;
    private final DivScreenRepository divScreenRepository;

    public Mono<DivKitPageEntity> getPageCacheable(String screenId) {
        return redisCache.getById(screenId)
                .switchIfEmpty(Mono.defer(() -> getPage(screenId)))
                .doOnNext(redisCache::save);
    }

    public Mono<DivKitPageEntity> getPage(String id) {
        return divScreenRepository.findById(id)
                .switchIfEmpty(Mono.error(new DivKitNotFoundException(id)));
    }

    public Flux<DivKitPageEntity> getAllPages() {
        return divScreenRepository.findAll();
    }

    public Mono<DivKitPageEntity> createPage(DivKitPageEntity model) {
        return validate(model)
                .flatMap(div -> divScreenRepository.findById(div.getPageId())
                        .flatMap(entity -> Mono.error(new DivKitExistsException(entity.getPageId())))
                )
                .switchIfEmpty(Mono.just(model))
                .flatMap(div -> divScreenRepository.save((DivKitPageEntity) div));
    }

    public Mono<DivKitPageEntity> updatePage(DivKitPageEntity model) {
        return validate(model)
                .flatMap(div -> divScreenRepository.findById(div.getPageId())
                        .switchIfEmpty(Mono.error(new DivKitNotFoundException(div.getPageId())))
                )
                .map(entity -> {
                    entity.setJson(model.getJson());
                    return entity;
                })
                .flatMap(divScreenRepository::save);
    }

    public Mono<Void> deletePage(String id) {
        return divScreenRepository.findById(id)
                .switchIfEmpty(Mono.error(new DivKitNotFoundException(id)))
                .flatMap(divScreenRepository::delete);
    }

    private Mono<DivKitPageEntity> validate(DivKitPageEntity page) {
        if (!page.getPageId().matches(VALIDATION_REGEX)) {
            return Mono.error(new FieldValidationException("id"));
        }
        if (page.getJson().isBlank()) {
            return Mono.error(new FieldValidationException("json"));
        }
        try {
            jsonMapper.readTree(page.getJson());
        } catch (IOException ex) {
            return Mono.error(new FieldValidationException("json"));
        }
        return Mono.just(page);
    }
}