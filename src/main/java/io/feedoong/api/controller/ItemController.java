package io.feedoong.api.controller;

import io.feedoong.api.controller.response.PageResponse;
import io.feedoong.api.domain.dto.ChannelItemDTO;
import io.feedoong.api.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/v2/items")
    public PageResponse<ChannelItemDTO> getItems(
            @AuthenticationPrincipal UserDetails requestUser,
            @PageableDefault(page = 0, size = 10, sort = "publishedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ChannelItemDTO> subscribedChannelsItems = itemService.getItems(pageable, requestUser);
        return new PageResponse<>(subscribedChannelsItems);
    }
}
