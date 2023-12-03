package io.feedoong.api.shared.factory;

import io.feedoong.api.domain.channel.Channel;
import io.feedoong.api.domain.channel.dto.ChannelDetailsDTO;

public class ChannelFactory {
    public static Channel create() {
        return Channel.builder()
                .title("feedoong.log")
                .description("")
                .url("https://velog.io/@feedoong")
                .feedUrl("https://v2.velog.io/rss/feedoong")
                .imageUrl("https://velog.velcdn.com/images/feedoong/profile/1c8a2eb9-dbd9-499d-b846-3d3855c94bc0/social_profile.png")
                .build();
    }

    public static ChannelDetailsDTO mockChannelDetailsDTO() {
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
