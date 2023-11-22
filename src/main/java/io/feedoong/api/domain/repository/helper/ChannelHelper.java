package io.feedoong.api.domain.repository.helper;

import io.feedoong.api.domain.Channel;
import io.feedoong.api.domain.User;
import io.feedoong.api.domain.repository.ChannelRepository;
import io.feedoong.api.service.dto.ChannelDetailsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ChannelHelper {
    private final ChannelRepository channelRepository;

    public Page<ChannelDetailsDTO> getSubscribedChannels(Pageable pageable, User user) {
        Page<Channel> subscribedChannels = channelRepository.findSubscribedChannelsByUser(pageable, user);
        List<ChannelDetailsDTO> channelDetailsDTOList = subscribedChannels.stream()
                .map(channel -> ChannelDetailsDTO.builder()
                        .id(channel.getId())
                        .title(channel.getTitle())
                        .description(channel.getDescription())
                        .url(channel.getUrl())
                        .feedUrl(channel.getFeedUrl())
                        .imageUrl(channel.getImageUrl())
                        .isSubscribed(true)
                        .build())
                .collect(Collectors.toList());
        return new PageImpl<>(channelDetailsDTOList, pageable, subscribedChannels.getTotalElements());
    }
}
