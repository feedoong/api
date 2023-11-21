package io.feedoong.api.service;

import io.feedoong.api.domain.Channel;
import io.feedoong.api.domain.Subscription;
import io.feedoong.api.domain.repository.SubscriptionRepository;
import io.feedoong.api.domain.repository.helper.UserHelper;
import io.feedoong.api.service.dto.ChannelDetailsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChannelService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserHelper userHelper;

    @Transactional(readOnly = true)
    public List<ChannelDetailsDTO> getRecommendedChannels(Pageable pageable, UserDetails requestUser) {
        // Pageable limit에 맞춰 가장 최근 구독된 채널 조회
        Page<Subscription> subscriptions = subscriptionRepository.findMostRecentSubscriptionsByChannel(pageable);
        List<Long> channelIds = getChannelIds(subscriptions.getContent());
        Set<Long> subscriptionIds = getSubscriptionIds(requestUser, channelIds).orElse(Collections.emptySet());

        return subscriptions.stream()
                .map(subscription -> {
                    Channel channel = subscription.getChannel();
                    boolean isSubscribed = subscriptionIds.contains(channel.getId());
                    return buildChannelDetailsDTO(channel, isSubscribed);
                })
                .collect(Collectors.toList());
    }

    private static List<Long> getChannelIds(List<Subscription> subscriptions) {
        return subscriptions.stream()
                .map(subscription -> subscription.getChannel().getId())
                .toList();
    }

    private Optional<Set<Long>> getSubscriptionIds(UserDetails requestUser, List<Long> channelIds) {
        return  Optional.ofNullable(requestUser)
                .map(UserDetails::getUsername)
                .map(userHelper::getByEmail)
                .map(user -> new HashSet<>(subscriptionRepository.findSubscribedChannelIdsByUser(user, channelIds)));
    }

    private static ChannelDetailsDTO buildChannelDetailsDTO(Channel channel, boolean isSubscribed) {
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
