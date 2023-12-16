package io.feedoong.api.controller.response;

import io.feedoong.api.service.dto.LikeItemDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LikeItemResponse {
    private Long itemId;
    private Boolean isLiked;

    public static LikeItemResponse of(LikeItemDTO dto) {
        return new LikeItemResponse(dto.getItemId(), dto.getIsLiked());
    }
}
