package io.feedoong.api.global.security.jwt;

import io.feedoong.api.domain.user.User;
import io.feedoong.api.shared.factory.UserFactory;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("TokenProvider 클래스")
class TokenProviderTest {

    final String SECRET_STRING = "UiEF7xBOSPRMCGwHlQAEk3IUlzOJWaZbRi3fpMtMNBA=";
    final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));
    final TokenProvider tokenProvider = new TokenProvider(SECRET_KEY);

    @Nested
    @DisplayName("create 메소드")
    class Create {

        @Nested
        @DisplayName("유효한 사용자 정보가 주어지면")
        class WithValidUser {

            private User user;

            @BeforeEach
            void prepare() {
                user = UserFactory.create();
            }

            @Test
            @DisplayName("올바른 형식의 JWT Token을 리턴한다.")
            public void should() throws Exception {
                String bearerToken = tokenProvider.create(user);

                assertThat(bearerToken).isNotNull();
            }
        }
    }

    @Nested
    @DisplayName("parseBearerToken 메소드")
    class ParseBearerToken {

        @Nested
        @DisplayName("올바른 형식의 Bearer 토큰이 주어지면")
        class WithValidBearerToken {

            String validBearerToken;

            @BeforeEach
            void prepare() {
                validBearerToken = "Bearer jwtToken";
            }

            @Test
            @DisplayName("\"Bearer\"를 제거한 토큰을 리턴한다.")
            public void should() throws Exception {
                String token = tokenProvider.parseBearerToken(validBearerToken);

                assertThat(token).isEqualTo("jwtToken");
            }
        }

        @Nested
        @DisplayName("올바르지 않은 형식의 토큰이 주어지면")
        class WithInvalidBearerToken {

            String invalidBearerToken;

            @BeforeEach
            void prepare() {
                invalidBearerToken = "Bear jwtToken";
            }

            @Test
            @DisplayName("BadCredentialsException을 던진다.")
            public void should() throws Exception {
                assertThatThrownBy(() -> tokenProvider.parseBearerToken(invalidBearerToken)).isInstanceOf(BadCredentialsException.class);
            }
        }
    }

    @Nested
    @DisplayName("validate 메소드")
    class Validate {

        @Nested
        @DisplayName("유효한 JWT 토큰이 주어지면")
        class WithValidJwtToken {

            private String validJwtToken;

            @BeforeEach
            void setUp() {
                User user = UserFactory.create();
                validJwtToken = tokenProvider.create(user);
            }

            @Test
            @DisplayName("예외를 발생시키지 않는다.")
            void shouldNotThrowException() {
                assertDoesNotThrow(() -> tokenProvider.validate(validJwtToken));
            }
        }

        @Nested
        @DisplayName("유효하지 않은 JWT 토큰이 주어지면")
        class WithInvalidJwtToken {

            private String invalidToken;

            @BeforeEach
            void prepare() {
                invalidToken = "invalid!";
            }

            @Test
            @DisplayName("JwtException을 던진다.")
            void shouldThrowJwtException() {
                assertThatThrownBy(() -> tokenProvider.validate(invalidToken)).isInstanceOf(JwtException.class);
            }
        }
    }

    @Nested
    @DisplayName("getSubject 메소드")
    class GetSubject {

        @Nested
        @DisplayName("유효한 JWT 토큰이 주어지면")
        class WithValidJwtToken {

            private User user;
            private String validJwtToken;

            @BeforeEach
            void setUp() {
                user = UserFactory.create();
                validJwtToken = tokenProvider.create(user);
            }

            @Test
            @DisplayName("토큰에서 올바른 사용자 이메일을 반환한다.")
            void shouldReturnCorrectEmail() {
                String result = tokenProvider.getSubject(validJwtToken);

                assertThat(result).isEqualTo(user.getEmail());
            }
        }
    }
}