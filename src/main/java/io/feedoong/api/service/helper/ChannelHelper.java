package io.feedoong.api.service.helper;

import io.feedoong.api.domain.channel.Channel;
import io.feedoong.api.domain.channel.dto.ChannelDetailsDTO;
import io.feedoong.api.domain.user.User;
import io.feedoong.api.domain.channel.ChannelRepository;
import io.feedoong.api.global.exception.CustomException;
import io.feedoong.api.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ChannelHelper {
    private final ChannelRepository channelRepository;

    private final String FEEDOONG_CHANNEL_URL = "https://velog.io/@feedoong";

    public Page<Channel> getSubscribedChannels(Pageable pageable, User user) {
        return channelRepository.findSubscribedChannelsByUser(pageable, user);
    }

    public Optional<Channel> findOptFeedoongChannel() {
        return channelRepository.findChannelByUrl(FEEDOONG_CHANNEL_URL);
    }

    public Channel getChannel(Long id) {
        return channelRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));
    }

    public ChannelDetailsDTO getChannelDetails(Optional<User> user, Long channelId) {
        return channelRepository.getChannelDetails(user, channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));
    }
}
