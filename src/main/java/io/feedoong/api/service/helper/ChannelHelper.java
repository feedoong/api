package io.feedoong.api.service.helper;

import io.feedoong.api.domain.Channel;
import io.feedoong.api.domain.User;
import io.feedoong.api.domain.repository.ChannelRepository;
import io.feedoong.api.global.exception.CustomException;
import io.feedoong.api.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ChannelHelper {
    private final ChannelRepository channelRepository;

    public Page<Channel> getSubscribedChannels(Pageable pageable, User user) {
        return channelRepository.findSubscribedChannelsByUser(pageable, user);
    }

    public Channel getChannel(Long id) {
        return channelRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));
    }
}
