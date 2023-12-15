package io.feedoong.api.controller.response;

import io.feedoong.api.service.dto.ViewedItemDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ViewItemResponse {
    private Long id;
    private Boolean isViewed;

    public static ViewItemResponse of(ViewedItemDTO dto) {
        return new ViewItemResponse(dto.getItemId(), dto.getIsViewed());
    }
}
