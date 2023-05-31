package ru.mephi.divkitpages.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.mephi.divkitpages.service.DivKitService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/divkit-pages")
public class DivKitController {
    private final DivKitService service;
    private final DivKitMapper mapper;

    @GetMapping("/{divId}")
    public Mono<DivKitPage> getPage(@PathVariable String divId) {
        return service.getPageCacheable(divId)
                .map(mapper::modelToDto);
    }
}
