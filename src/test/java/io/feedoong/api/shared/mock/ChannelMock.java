package io.feedoong.api.shared.mock;

import io.feedoong.api.domain.channel.dto.ChannelDetailsDTO;

public class ChannelMock {
    public static ChannelDetailsDTO channelDetailsDTO() {
        return ChannelDetailsDTO.builder()
                .id(1L)
                .title("토스테크")
                .description("토스의 개발과 디자인에 대한 이야기를 다룹니다.")
                .url("https://toss.tech")
                .feedUrl("https://toss.tech/rss.xml")
                .imageUrl("https://toss.tech/image.png")
                .isSubscribed(true)
                .build();
    }
}
