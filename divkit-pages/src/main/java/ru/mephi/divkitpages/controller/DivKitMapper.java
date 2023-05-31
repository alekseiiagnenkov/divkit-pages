package ru.mephi.divkitpages.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.mephi.divkitpages.service.DivKitPageEntity;

@Component
@RequiredArgsConstructor
public class DivKitMapper {

    private final ObjectMapper jsonMapper;

    @SneakyThrows
    public DivKitPage modelToDto(DivKitPageEntity entity) {
        return DivKitPage.builder()
                .div(jsonMapper.readValue(entity.getJson(), Object.class))
                .build();
    }
}
