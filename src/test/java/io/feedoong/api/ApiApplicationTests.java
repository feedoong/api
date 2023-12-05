package io.feedoong.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@DisplayName("ApiApplication 클래스")
class ApiApplicationTests {

    @Test
    @DisplayName("main 메소드 테스트")
    void apiApplicationTest() {
        try (MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {
            mocked.when(() -> SpringApplication.run((Class<?>) any(), any())).thenReturn(null);
            ApiApplication.main(new String[]{});
            mocked.verify(() -> SpringApplication.run(ApiApplication.class, new String[]{}));
        }
    }
}
