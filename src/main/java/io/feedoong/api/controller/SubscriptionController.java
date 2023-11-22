package io.feedoong.api.controller;

import io.feedoong.api.controller.response.PageResponse;
import io.feedoong.api.service.SubscriptionService;
import io.feedoong.api.service.dto.ChannelDetailsDTO;
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
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @GetMapping("/v2/subscriptions")
    public PageResponse<ChannelDetailsDTO> getSubscriptions(
            @AuthenticationPrincipal UserDetails requestUser,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ChannelDetailsDTO> subscribedChannels = subscriptionService.getSubscribedChannels(pageable, requestUser);
        return new PageResponse<>(subscribedChannels);
    }
}
