package io.feedoong.api.controller.dto;

import io.feedoong.api.service.dto.ChannelDetailsDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendedChannelsResponse {
    private List<ChannelDetailsDTO> channels;
}
