package io.feedoong.api.controller;

import io.feedoong.api.domain.Channel;
import io.feedoong.api.domain.Subscription;
import io.feedoong.api.domain.User;
import io.feedoong.api.domain.repository.ChannelRepository;
import io.feedoong.api.domain.repository.SubscriptionRepository;
import io.feedoong.api.domain.repository.UserRepository;
import io.feedoong.api.global.security.jwt.TokenProvider;
import io.feedoong.api.shared.factory.ChannelFactory;
import io.feedoong.api.shared.factory.SubscriptionFactory;
import io.feedoong.api.shared.factory.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static io.feedoong.api.shared.util.ApiDocumentationUtils.fromRequest;
import static io.feedoong.api.shared.util.ApiDocumentationUtils.fromResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@DisplayName("SubscriptionController 클래스 RestDocs")
class SubscriptionControllerDocsTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Nested
    @DisplayName("GET /v2/subscriptions - getSubscriptions 메소드")
    class GetSubscriptions {
        private Channel channel;
        private User user;
        private Subscription subscription;
        private String token;

        @BeforeEach
        void prepare() {
            user = UserFactory.create();
            userRepository.save(user);

            channel = ChannelFactory.create();
            channelRepository.save(channel);

            subscription = SubscriptionFactory.create(channel, user);
            subscriptionRepository.save(subscription);

            token = tokenProvider.create(user);
        }

        @Test
        @DisplayName("성공 테스트")
        public void should() throws Exception {
            mockMvc.perform(get("/v2/subscriptions")
                            .contentType("application/json")
                            .header("Authorization", token)
                            .param("page", "0")
                            .param("size", "10")
                            .param("sort", "createdAt")
                            .param("direction", "desc"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(jsonPath("$.totalElements").value(1))
                    .andExpect(jsonPath("$.page").value(0))
                    .andExpect(jsonPath("$.size").value(10))
                    .andDo(document("v2/subscriptions",
                            fromRequest(),
                            fromResponse(),
                            queryParameters(
                                    parameterWithName("page").description("페이지 번호 (0부터 시작)"),
                                    parameterWithName("size").description("한 페이지 당 반환되는 항목의 수"),
                                    parameterWithName("sort").description("정렬 기준이 되는 필드 (예: 'createdAt')"),
                                    parameterWithName("direction").description("정렬 방향 (asc 또는 desc)")
                            ),
                            PayloadDocumentation.responseFields(
                                    fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수"),
                                    fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("총 요소 수"),
                                    fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                                    fieldWithPath("size").type(JsonFieldType.NUMBER).description("페이지 당 항목 수"),
                                    fieldWithPath("contents").type(JsonFieldType.ARRAY).description("현재 페이지의 콘텐츠 목록"),
                                    fieldWithPath("contents[].id").type(JsonFieldType.NUMBER).description("채널 ID"),
                                    fieldWithPath("contents[].title").type(JsonFieldType.STRING).description("채널 제목"),
                                    fieldWithPath("contents[].description").type(JsonFieldType.STRING).description("채널 설명"),
                                    fieldWithPath("contents[].url").type(JsonFieldType.STRING).description("채널 URL"),
                                    fieldWithPath("contents[].feedUrl").type(JsonFieldType.STRING).description("피드 URL"),
                                    fieldWithPath("contents[].imageUrl").type(JsonFieldType.STRING).description("이미지 URL"),
                                    fieldWithPath("contents[].isSubscribed").type(JsonFieldType.BOOLEAN).description("구독 여부")
                            )
                    ));
        }
    }
}