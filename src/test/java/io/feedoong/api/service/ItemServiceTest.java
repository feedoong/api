package io.feedoong.api.service;

import io.feedoong.api.domain.item.dto.ChannelItemDTO;
import io.feedoong.api.domain.user.User;
import io.feedoong.api.global.security.CustomUserDetails;
import io.feedoong.api.service.helper.ChannelHelper;
import io.feedoong.api.service.helper.ItemHelper;
import io.feedoong.api.service.helper.UserHelper;
import io.feedoong.api.shared.base.BaseServiceTest;
import io.feedoong.api.shared.factory.ItemFactory;
import io.feedoong.api.shared.factory.UserFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@DisplayName("ItemService 클래스")
class ItemServiceTest extends BaseServiceTest {
    @Mock
    private UserHelper userHelper;

    @Mock
    private ItemHelper itemHelper;

    @Mock
    private ChannelHelper channelHelper;

    @InjectMocks
    private ItemService itemService;

    @Nested
    @DisplayName("findLikedItems 메소드")
    class FindLikedItems {

        @Nested
        @DisplayName("로그인한 유저가 좋아요 누른 아이템이 존재하면")
        class WithLikedItems {
            private UserDetails requestUser;

            @BeforeEach
            void prepare() {
                User user = UserFactory.create();
                when(userHelper.findByEmail(anyString()))
                        .thenReturn(user);

                requestUser = new CustomUserDetails(user);

                List<ChannelItemDTO> channelItemDTOS = ItemFactory.mockChannelItemDTOs();
                when(itemHelper.findLikedItems(any(Pageable.class), any(User.class)))
                        .thenReturn(new PageImpl<>(channelItemDTOS));
            }

            @Test
            @DisplayName("좋아요 누른 아이템이 담겨있는 Page 객체를 성공적으로 리턴한다.")
            public void shouldReturn_PageObject_With_LikedItems() throws Exception {
                Page<ChannelItemDTO> result = itemService.findLikedItems(Pageable.unpaged(), requestUser);

                Assertions.assertThat(result.getContent()).isNotEmpty();
            }
        }
    }
}