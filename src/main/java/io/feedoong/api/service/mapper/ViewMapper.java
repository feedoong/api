package io.feedoong.api.service.mapper;

import io.feedoong.api.domain.item.Item;
import io.feedoong.api.service.dto.ViewedItemDTO;

public class ViewMapper {
    public static ViewedItemDTO toViewedItemDTO(Item item, boolean isViewed) {
        return ViewedItemDTO.builder()
                .itemId(item.getId())
                .isViewed(isViewed)
                .build();
    }
}
