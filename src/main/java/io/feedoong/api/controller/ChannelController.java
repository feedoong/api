package io.feedoong.api.controller;

import io.feedoong.api.controller.dto.RecommendedChannelsResponse;
import io.feedoong.api.service.ChannelService;
import io.feedoong.api.service.dto.ChannelDetailsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/v1/channels")
@RestController
public class ChannelController {

    private final ChannelService channelService;

    @GetMapping("/recommended")
    public ResponseEntity<RecommendedChannelsResponse> getRecommendedChannels(
            @PageableDefault(size = 20, direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal UserDetails requestUser
    ) {
        List<ChannelDetailsDTO> recommendedChannels = channelService.getRecommendedChannels(pageable, requestUser);
        RecommendedChannelsResponse recommendedChannelsResponse = new RecommendedChannelsResponse(recommendedChannels);
        return ResponseEntity.status(HttpStatus.OK).body(recommendedChannelsResponse);
    }
}
