package io.feedoong.api.service.helper;

import io.feedoong.api.domain.user.User;
import io.feedoong.api.domain.user.UserRepository;
import io.feedoong.api.global.exception.ErrorCode;
import io.feedoong.api.shared.base.BaseHelperTest;
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
import static org.mockito.Mockito.when;

@DisplayName("UserHelper 클래스")
class UserHelperTest extends BaseHelperTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserHelper userHelper;

    @Nested
    @DisplayName("findByEmail 메소드")
    class FindByEmail {

        @Nested
        @DisplayName("email에 해당하는 user가 존재하지 않으면")
        class WhenEmailNotExists {

            private String email;

            @BeforeEach
            void prepare() {
                email = "wrong@gmail.com";
                when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
            }

            @Test
            @DisplayName("USER_NOT_FOUND CustomException을 던진다.")
            public void shouldThrow_USER_NOT_FOUND_CustomException() throws Exception {
                assertThrowsCustomException(() -> userHelper.findByEmail(email)).with(ErrorCode.USER_NOT_FOUND);
            }
        }

        @Nested
        @DisplayName("email에 해당하는 user가 존재하면")
        class WhenEmailExists {

            private User user;
            private String email;

            @BeforeEach
            void prepare() {
                user = UserFactory.create();
                email = user.getEmail();
                when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
            }

            @Test
            @DisplayName("조회된 User를 리턴한다.")
            public void shouldReturn_User() throws Exception {
                User result = userHelper.findByEmail(email);

                assertThat(result).usingRecursiveComparison().isEqualTo(user);
            }
        }
    }
}