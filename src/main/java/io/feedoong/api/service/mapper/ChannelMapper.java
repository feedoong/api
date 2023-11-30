package io.feedoong.api.service.mapper;

import io.feedoong.api.domain.channel.Channel;
import io.feedoong.api.service.dto.ChannelDetailsDTO;

public class ChannelMapper {
    public static ChannelDetailsDTO toChannelDetailsTDTO(Channel channel, boolean isSubscribed) {
        return ChannelDetailsDTO.builder()
                .id(channel.getId())
                .title(channel.getTitle())
                .description(channel.getDescription())
                .url(channel.getUrl())
                .feedUrl(channel.getFeedUrl())
                .imageUrl(channel.getImageUrl())
                .isSubscribed(isSubscribed)
                .build();
    }
}
