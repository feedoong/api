package io.feedoong.api.service;

import io.feedoong.api.domain.item.Item;
import io.feedoong.api.domain.like.Like;
import io.feedoong.api.domain.user.User;
import io.feedoong.api.global.exception.ErrorCode;
import io.feedoong.api.global.security.CustomUserDetails;
import io.feedoong.api.service.dto.LikeItemDTO;
import io.feedoong.api.service.helper.ItemHelper;
import io.feedoong.api.service.helper.LikeHelper;
import io.feedoong.api.service.helper.UserHelper;
import io.feedoong.api.shared.base.BaseServiceTest;
import io.feedoong.api.shared.factory.ItemFactory;
import io.feedoong.api.shared.factory.LikeFactory;
import io.feedoong.api.shared.factory.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static io.feedoong.api.shared.util.TestAssertionUtils.assertThrowsCustomException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@DisplayName("LikeService 클래스")
class LikeServiceTest extends BaseServiceTest {
    @Mock
    private UserHelper userHelper;

    @Mock
    private ItemHelper itemHelper;

    @Mock
    private LikeHelper likeHelper;

    @InjectMocks
    private LikeService likeService;

    @Nested
    @DisplayName("like 메소드")
    class LikeMethod {
        private User user;
        private Item item;
        private UserDetails requestUser;

        @BeforeEach
        void prepare() {
            user = UserFactory.create();
            item = ItemFactory.createWithID(null);
            requestUser = new CustomUserDetails(user);

            when(userHelper.findByEmail(anyString())).thenReturn(user);
            when(itemHelper.findById(anyLong())).thenReturn(item);
        }

        @Nested
        @DisplayName("이미 좋아요한 아이템이면")
        class WithLike {
            @BeforeEach
            void prepare() {
                Like like = LikeFactory.create(user, item);
                when(likeHelper.findByUserAndItem(any(User.class), any(Item.class)))
                        .thenReturn(Optional.of(like));
            }

            @Test
            @DisplayName("ALREADY_LIKED CustomException을 던진다.")
            public void shouldThrow_ALREADY_LIKED_CustomException() throws Exception {
                assertThrowsCustomException(() -> likeService.like(requestUser, item.getId())).with(ErrorCode.ALREADY_LIKED);
            }
        }

        @Nested
        @DisplayName("좋아요하지 않은 아이템이면")
        class WithoutLike {
            @Nested
            @DisplayName("새로운 좋아요를 생성하고")
            class CreateLike {
                @BeforeEach
                void prepare() {
                    Like like = LikeFactory.create(user, item);
                    when(likeHelper.save(any(Like.class))).thenReturn(like);
                }

                @Test
                @DisplayName("좋아요 결과를 리턴한다.")
                public void shouldThrow_Like() throws Exception {
                    LikeItemDTO result = likeService.like(requestUser, item.getId());

                    assertThat(result).isNotNull();
                    assertThat(result.getItemId()).isEqualTo(item.getId());
                    assertThat(result.getIsLiked()).isTrue();
                }
            }
        }
    }
}