package io.feedoong.api.shared.factory;

import io.feedoong.api.domain.channel.Channel;
import io.feedoong.api.domain.item.dto.ChannelItemDTO;
import io.feedoong.api.domain.item.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public static Item createWithID(Channel channel) {
        return Item.builder()
                .id(1L)
                .title("item")
                .description("description")
                .link("link.com")
                .guid("https://jojoldu.tistory.com/686")
                .publishedAt(LocalDateTime.of(2023, 1, 1, 10, 00 , 00))
                .imageUrl("https://feedoong.io/img.png")
                .channel(channel)
                .build();
    }

    public static List<Item> createItems(Channel channel) {
        List<Item> items = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Item item = Item.builder()
                    .title("title" + i)
                    .description("description" + i)
                    .link("link" + i + ".com")
                    .guid("https://feedoong.io/" + i)
                    .publishedAt(LocalDateTime.of(2023, 1, 1, 10, 00, 00))
                    .imageUrl("https://feedoong.io/" + i + ".png")
                    .channel(channel)
                    .build();
            items.add(item);
        }

        return items;
    }

    public static List<ChannelItemDTO> mockChannelItemDTOs() {
        ChannelItemDTO item1 = ChannelItemDTO.builder()
                .id(1L)
                .title("if kakao 2022 개발자 컨퍼런스를 개최합니다.")
                .description("kakao 컨퍼런스 개최")
                .link("https://tech.kakao.com/?p=20343")
                .guid("https://tech.kakao.com/?p=20343")
                .publishedAt(LocalDateTime.of(2022, 11, 23, 07, 43, 27))
                .imageUrl("https://teck.kakao.com/image.png")
                .isLiked(true)
                .isViewed(true)
                .channelId(1L)
                .channelTitle("tech.kakao.com")
                .channelImageUrl("https://teck.kakao.com/image.png")
                .build();

        return List.of(item1);
    }
}
