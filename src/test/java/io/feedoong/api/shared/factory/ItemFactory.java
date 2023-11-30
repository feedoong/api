package io.feedoong.api.shared.factory;

import io.feedoong.api.domain.channel.Channel;
import io.feedoong.api.domain.item.Item;

import java.time.LocalDateTime;

public class ItemFactory {
    public static Item create(Channel channel) {
        return Item.builder()
                .title("item")
                .description("description")
                .link("link.com")
                .guid("https://jojoldu.tistory.com/686")
                .publishedAt(LocalDateTime.of(2023, 1, 1, 10, 00 , 00))
                .imageUrl("https://feedoong.io/img.png")
                .channel(channel)
                .build();
    }
}
