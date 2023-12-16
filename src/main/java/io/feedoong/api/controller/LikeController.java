package io.feedoong.api.controller;

import io.feedoong.api.controller.response.LikeItemResponse;
import io.feedoong.api.service.LikeService;
import io.feedoong.api.service.dto.LikeItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/v2/likes/item/{itemId}")
    public void unlike(
            @AuthenticationPrincipal UserDetails requestUser,
            @PathVariable(value = "itemId") Long itemId
    ) {
        likeService.unlike(requestUser, itemId);
    }
}
