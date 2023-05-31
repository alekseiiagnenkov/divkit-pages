package ru.mephi.divkitpages.service;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DivKitPageEntity {
    String pageId;
    String json;
}
