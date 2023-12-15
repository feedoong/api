package io.feedoong.api.service;

import io.feedoong.api.domain.item.Item;
import io.feedoong.api.domain.user.User;
import io.feedoong.api.domain.view.View;
import io.feedoong.api.global.security.CustomUserDetails;
import io.feedoong.api.service.dto.ViewedItemDTO;
import io.feedoong.api.service.helper.ItemHelper;
import io.feedoong.api.service.helper.UserHelper;
import io.feedoong.api.service.helper.ViewHelper;
import io.feedoong.api.shared.base.BaseServiceTest;
import io.feedoong.api.shared.factory.ItemFactory;
import io.feedoong.api.shared.factory.UserFactory;
import io.feedoong.api.shared.factory.ViewFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("ViewService 클래스")
class ViewServiceTest extends BaseServiceTest {
    @Mock
    private UserHelper userHelper;

    @Mock
    private ItemHelper itemHelper;

    @Mock
    private ViewHelper viewHelper;

    @InjectMocks
    private ViewService viewService;

    @Nested
    @DisplayName("viewItem 메소드")
    class ViewItem {
        private UserDetails requestUser;
        private User user;
        private Item item;

        @BeforeEach
        void prepare() {
            user = UserFactory.create();
            requestUser = new CustomUserDetails(user);
            item = ItemFactory.createWithID(null);

            when(userHelper.findByEmail(anyString())).thenReturn(user);
            when(itemHelper.findById(anyLong())).thenReturn(item);
        }

        @Nested
        @DisplayName("이미 View가 존재하면")
        class WithView {
            @BeforeEach
            void prepare() {
                View view = ViewFactory.create(user, item);
                when(viewHelper.findOptionalItemView(any(User.class), any(Item.class)))
                        .thenReturn(Optional.of(view));
            }

            @Test
            @DisplayName("View를 한 번 더 생성하지 않고, 결과를 리턴한다.")
            public void shouldNotSave_View() throws Exception {
                ViewedItemDTO result = viewService.viewItem(requestUser, item.getId());

                verify(viewHelper, times(0)).save(any(View.class));
                assertThat(result.getItemId()).isEqualTo(item.getId());
                assertThat(result.getIsViewed()).isEqualTo(true);
            }
        }

        @Nested
        @DisplayName("View가 존재하지 않으면")
        class WithoutView {
            @BeforeEach
            void prepare() {
                when(viewHelper.findOptionalItemView(any(User.class), any(Item.class)))
                        .thenReturn(Optional.empty());
            }

            @Test
            @DisplayName("View를 생성하고, 결과를 리턴한다.")
            public void should() throws Exception {
                ViewedItemDTO result = viewService.viewItem(requestUser, item.getId());

                verify(viewHelper, times(1)).save(any(View.class));
                assertThat(result.getItemId()).isEqualTo(item.getId());
                assertThat(result.getIsViewed()).isEqualTo(true);
            }
        }
    }
}