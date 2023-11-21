package io.feedoong.api.domain.repository;

import io.feedoong.api.domain.User;
import io.feedoong.api.shared.base.BaseRepositoryTest;
import io.feedoong.api.shared.factory.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserRepository 인터페이스")
class UserRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Nested
    @DisplayName("findByEmail 메소드")
    class FindByEmail {

        @Nested
        @DisplayName("email에 해당하는 user가 존재하지 않으면")
        class WhenEmailDoesNotExist {

            @Test
            @DisplayName("Empty Optional을 리턴한다.")
            public void shouldReturn_EmptyOptional() throws Exception {
                Optional<User> result = userRepository.findByEmail("bigfanoftim@gmail.com");

                assertThat(result).isEmpty();
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
                userRepository.save(user);
            }

            @Test
            @DisplayName("조회된 User Entity를 담은 Optional을 리턴한다.")
            public void shouldReturn_Optional_With_User() throws Exception {
                Optional<User> result = userRepository.findByEmail(email);

                assertThat(result).isPresent();
                assertThat(result.get()).usingRecursiveComparison().isEqualTo(user);
            }
        }
    }
}