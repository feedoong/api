package io.feedoong.api.controller;

import io.feedoong.api.controller.response.ViewItemResponse;
import io.feedoong.api.service.ViewService;
import io.feedoong.api.service.dto.ViewedItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ViewController {
    private final ViewService viewService;

    @PostMapping("/v2/views/item/{itemId}")
    public ViewItemResponse viewItem(
            @AuthenticationPrincipal UserDetails requestUser,
            @PathVariable(value = "itemId") Long itemId
    ) {
        ViewedItemDTO viewedItemDTO = viewService.viewItem(requestUser, itemId);
        return ViewItemResponse.of(viewedItemDTO);
    }
}
