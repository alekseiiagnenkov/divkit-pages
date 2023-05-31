package ru.mephi.divkitpages.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import ru.mephi.gprc.divkitpages.AllDivKitResponse;
import ru.mephi.gprc.divkitpages.DivKitCreateRequest;
import ru.mephi.gprc.divkitpages.DivKitResponse;
import ru.mephi.gprc.divkitpages.DivKitUpdateRequest;

@Component
public class DivKitGrpcMapper {
    public DivKitResponse modelToDto(DivKitPageEntity model) {
        return DivKitResponse.newBuilder()
                .setId(model.getPageId())
                .setJson(model.getJson())
                .build();
    }

    public DivKitPageEntity dtoToModel(DivKitCreateRequest dto) {
        return DivKitPageEntity.builder()
                .pageId(dto.getId())
                .json(dto.getJson())
                .build();
    }

    public DivKitPageEntity dtoToModel(DivKitUpdateRequest dto) {
        return DivKitPageEntity.builder()
                .pageId(dto.getId())
                .json(dto.getJson())
                .build();
    }

    AllDivKitResponse modelsToDto(List<DivKitPageEntity> models) {
        List<DivKitResponse> dtos = models.stream()
                .map(this::modelToDto)
                .collect(Collectors.toList());

        return AllDivKitResponse.newBuilder()
                .addAllDivKitResponse(dtos)
                .build();
    }
}
