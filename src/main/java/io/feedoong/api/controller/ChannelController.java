package io.feedoong.api.controller;

import io.feedoong.api.controller.response.Response;
import io.feedoong.api.domain.channel.dto.ChannelDetailsDTO;
import io.feedoong.api.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChannelController {
    private final ChannelService channelService;

    @GetMapping("/v2/channels/{channelId}")
    public Response<ChannelDetailsDTO> getChannelDetails(
            @AuthenticationPrincipal UserDetails requestUser,
            @PathVariable(value = "channelId") Long channelId
    ) {
        ChannelDetailsDTO channelDetails = channelService.getChannelDetails(channelId, requestUser);
        return new Response<>(channelDetails);
    }
}
