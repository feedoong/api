package io.feedoong.api.service;

import io.feedoong.api.domain.channel.Channel;
import io.feedoong.api.domain.subscription.Subscription;
import io.feedoong.api.domain.user.User;
import io.feedoong.api.service.dto.ChannelDetailsDTO;
import io.feedoong.api.service.helper.ChannelHelper;
import io.feedoong.api.service.helper.SubscriptionHelper;
import io.feedoong.api.service.helper.UserHelper;
import io.feedoong.api.service.mapper.ChannelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SubscriptionService {
    private final ChannelHelper channelHelper;
    private final UserHelper userHelper;
    private final SubscriptionHelper subscriptionHelper;

    public Page<ChannelDetailsDTO> getSubscribedChannels(Pageable pageable, UserDetails requestUser) {
        User user = userHelper.getByEmail(requestUser.getUsername());
        Page<Channel> subscribedChannels = channelHelper.getSubscribedChannels(pageable, user);
        boolean isSubscribed = true;
        List<ChannelDetailsDTO> channelDetailsDTOList = subscribedChannels.stream()
                .map(channel -> ChannelMapper.toChannelDetailsTDTO(channel, isSubscribed))
                .collect(Collectors.toList());
        return new PageImpl<>(channelDetailsDTOList, pageable, subscribedChannels.getTotalElements());
    }

    @Transactional
    public void unsubscribe(UserDetails requestUser, Long channelId) {
        User user = userHelper.getByEmail(requestUser.getUsername());
        Channel channel = channelHelper.getChannel(channelId);
        Subscription subscription = subscriptionHelper.getSubscription(user, channel);
        subscriptionHelper.delete(subscription);
    }
}
