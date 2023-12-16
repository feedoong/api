package io.feedoong.api.controller;

import io.feedoong.api.controller.response.LikeItemResponse;
import io.feedoong.api.service.LikeService;
import io.feedoong.api.service.dto.LikeItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/v2/likes/item/{itemId}")
    public LikeItemResponse like(
            @AuthenticationPrincipal UserDetails requestUser,
            @PathVariable(value = "itemId") Long itemId
    ) {
        LikeItemDTO likeItemDTO = likeService.like(requestUser, itemId);
        return LikeItemResponse.of(likeItemDTO);
    }
}
