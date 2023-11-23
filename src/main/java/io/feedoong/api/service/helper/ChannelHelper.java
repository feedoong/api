package io.feedoong.api.service.helper;

import io.feedoong.api.domain.Channel;
import io.feedoong.api.domain.User;
import io.feedoong.api.domain.repository.ChannelRepository;
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
}
