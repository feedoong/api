package io.feedoong.api.service.helper;

import io.feedoong.api.domain.item.Item;
import io.feedoong.api.domain.like.Like;
import io.feedoong.api.domain.like.LikeRepository;
import io.feedoong.api.domain.user.User;
import io.feedoong.api.global.exception.ErrorCode;
import io.feedoong.api.shared.base.BaseHelperTest;
import io.feedoong.api.shared.factory.ItemFactory;
import io.feedoong.api.shared.factory.LikeFactory;
import io.feedoong.api.shared.factory.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static io.feedoong.api.shared.util.TestAssertionUtils.assertThrowsCustomException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("LikeHelper 클래스")
class LikeHelperTest extends BaseHelperTest {
    @Mock
    private LikeRepository likeRepository;

    @InjectMocks
    private LikeHelper likeHelper;

    @Nested
    @DisplayName("findOne 메소드")
    class FindOneMethodTest {
        @Nested
        @DisplayName("주어진 아이템, 사용자에 대한 좋아요가")
        class LikeByUserAndItem {
            private User user;
            private Item item;

            @BeforeEach
            void prepare() {
                user = UserFactory.create();
                item = ItemFactory.createWithID(null);
            }

            @Nested
            @DisplayName("없으면")
            class Without {
                @BeforeEach
                void prepare() {
                    when(likeRepository.findByUserAndItem(any(User.class), any(Item.class)))
                            .thenReturn(Optional.empty());
                }

                @Test
                @DisplayName("LIKE_NOT_FOUND CustomException을 던진다.")
                public void shouldThrow_LIKE_NOT_FOUND_CustomException() throws Exception {
                    assertThrowsCustomException(() -> likeHelper.findOne(user ,item))
                            .with(ErrorCode.LIKE_NOT_FOUND);
                }
            }

            @Nested
            @DisplayName("있으면")
            class With {
                @BeforeEach
                void prepare() {
                    Like like = LikeFactory.create(user, item);
                    when(likeRepository.findByUserAndItem(any(User.class), any(Item.class)))
                            .thenReturn(Optional.of(like));
                }

                @Test
                @DisplayName("조회한 좋아요를 리턴한다.")
                public void shouldReturn_Like() throws Exception {
                    Like result = likeHelper.findOne(user, item);

                    assertThat(result).isNotNull();
                }
            }
        }
    }
}