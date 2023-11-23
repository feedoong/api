package io.feedoong.api.service;

import io.feedoong.api.domain.Channel;
import io.feedoong.api.domain.User;
import io.feedoong.api.service.dto.ChannelDetailsDTO;
import io.feedoong.api.service.helper.ChannelHelper;
import io.feedoong.api.service.helper.UserHelper;
import io.feedoong.api.service.mapper.ChannelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SubscriptionService {
    private final ChannelHelper channelHelper;
    private final UserHelper userHelper;

    public Page<ChannelDetailsDTO> getSubscribedChannels(Pageable pageable, UserDetails requestUser) {
        User user = userHelper.getByEmail(requestUser.getUsername());
        Page<Channel> subscribedChannels = channelHelper.getSubscribedChannels(pageable, user);
        boolean isSubscribed = true;
        List<ChannelDetailsDTO> channelDetailsDTOList = subscribedChannels.stream()
                .map(channel -> ChannelMapper.toChannelDetailsTDTO(channel, isSubscribed))
                .collect(Collectors.toList());
        return new PageImpl<>(channelDetailsDTOList, pageable, subscribedChannels.getTotalElements());
    }
}
