package io.feedoong.api.global.resolver;

import io.feedoong.api.global.exception.ErrorCode;
import io.feedoong.api.global.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
public class PageableVerificationArgumentResolverTest {

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(new PageableVerificationArgumentResolver())
                .build();
    }

    @Nested
    @DisplayName("유효한 pageable 정보가 주어지면")
    class WithValidPageable {

        @Test
        @DisplayName("정상적인 응답 'ok'를 리턴한다.")
        public void shouldReturn_OK_ForValidPageable() throws Exception {
            mockMvc.perform(get("/test")
                            .param("page", "1")
                            .param("size", "5"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("ok"));
        }
    }

    @Nested
    @DisplayName("유효하지 않은 pageNumber가 주어지면")
    class WithInvalidPageNumber {

        @Test
        @DisplayName("INVALID_PAGE_NUMBER CustomException을 던진다.")
        public void shouldThrow_CustomException_For_InvalidPageNumber() throws Exception {
            mockMvc.perform(get("/test")
                            .param("page", "-1")
                            .param("size", "5"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value(ErrorCode.INVALID_PAGE_NUMBER.toString()));
        }
    }

    @Nested
    @DisplayName("유효하지 않은 pageSize가 주어지면")
    class WithInvalidPageSize {

        @Test
        @DisplayName("INVALID_PAGE_SIZE CustomException을 던진다.")
        public void shouldThrow_CustomException_For_InvalidPageSize() throws Exception {
            mockMvc.perform(get("/test")
                            .param("page", "0")
                            .param("size", "0"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value(ErrorCode.INVALID_PAGE_SIZE.toString()));
        }
    }

    @RestController
    private static class TestController {

        @GetMapping("/test")
        public String test(Pageable pageable) {
            return "ok";
        }
    }
}
