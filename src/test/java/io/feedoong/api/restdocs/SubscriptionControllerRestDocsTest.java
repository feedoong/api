package io.feedoong.api.restdocs;

import io.feedoong.api.domain.channel.Channel;
import io.feedoong.api.domain.channel.ChannelRepository;
import io.feedoong.api.domain.subscription.Subscription;
import io.feedoong.api.domain.subscription.SubscriptionRepository;
import io.feedoong.api.domain.user.User;
import io.feedoong.api.domain.user.UserRepository;
import io.feedoong.api.shared.base.BaseRestDocsTest;
import io.feedoong.api.shared.factory.ChannelFactory;
import io.feedoong.api.shared.factory.SubscriptionFactory;
import io.feedoong.api.shared.factory.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.payload.JsonFieldType;

import static io.feedoong.api.shared.util.ApiDocumentationUtils.fromRequest;
import static io.feedoong.api.shared.util.ApiDocumentationUtils.fromResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@DisplayName("SubscriptionController REST Docs")
class SubscriptionControllerRestDocsTest extends BaseRestDocsTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Nested
    @DisplayName("GET /v2/subscriptions - getSubscriptions 메소드")
    class GetSubscriptions {
        private String token;

        @BeforeEach
        void prepare() {
            User user = UserFactory.create();
            userRepository.save(user);

            Channel channel = ChannelFactory.create();
            channelRepository.save(channel);

            Subscription subscription = SubscriptionFactory.create(channel, user);
            subscriptionRepository.save(subscription);

            token = tokenProvider.create(user);
        }

        @Test
        public void success() throws Exception {
            mockMvc.perform(get("/v2/subscriptions")
                            .contentType("application/json")
                            .header("Authorization", token)
                            .param("page", "0")
                            .param("size", "10")
                            .param("sort", "createdAt")
                            .param("direction", "desc"))
                    .andDo(print())
                    .andDo(document("v2/subscriptions",
                            fromRequest(),
                            fromResponse(),
                            queryParameters(
                                    parameterWithName("page").description("페이지 번호 (0부터 시작)"),
                                    parameterWithName("size").description("한 페이지 당 반환되는 항목의 수"),
                                    parameterWithName("sort").description("정렬 기준이 되는 필드 (예: 'createdAt')"),
                                    parameterWithName("direction").description("정렬 방향 (asc 또는 desc)")
                            ),
                            responseFields(
                                    fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수"),
                                    fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("총 요소 수"),
                                    fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                                    fieldWithPath("size").type(JsonFieldType.NUMBER).description("페이지 당 항목 수"),
                                    fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부"),
                                    fieldWithPath("isFirst").type(JsonFieldType.BOOLEAN).description("현재 페이지가 첫 번째 페이지인지 여부"),
                                    fieldWithPath("isLast").type(JsonFieldType.BOOLEAN).description("현재 페이지가 마지막 페이지인지 여부"),
                                    fieldWithPath("contents").type(JsonFieldType.ARRAY).description("현재 페이지의 콘텐츠 목록 (ChannelDetailsDTO)"),
                                    fieldWithPath("contents[].id").type(JsonFieldType.NUMBER).description("채널 ID"),
                                    fieldWithPath("contents[].title").type(JsonFieldType.STRING).description("채널 제목"),
                                    fieldWithPath("contents[].description").type(JsonFieldType.STRING).description("채널 설명"),
                                    fieldWithPath("contents[].url").type(JsonFieldType.STRING).description("채널 URL"),
                                    fieldWithPath("contents[].feedUrl").type(JsonFieldType.STRING).description("피드 URL"),
                                    fieldWithPath("contents[].imageUrl").type(JsonFieldType.STRING).description("이미지 URL"),
                                    fieldWithPath("contents[].isSubscribed").type(JsonFieldType.BOOLEAN).description("구독 여부, 이 API에서 항상 true")
                            )
                    ));
        }
    }

    @Nested
    @DisplayName("DELETE /v2/subscriptions/channels/{channelId} - unsubscribe 메소드")
    class Unsubscribe {
        private Channel channel;
        private String token;

        @BeforeEach
        void prepare() {
            User user = UserFactory.create();
            userRepository.save(user);

            channel = ChannelFactory.create();
            channelRepository.save(channel);

            Subscription subscription = SubscriptionFactory.create(channel, user);
            subscriptionRepository.save(subscription);

            token = tokenProvider.create(user);
        }

        @Test
        public void success() throws Exception {
            mockMvc.perform(delete("/v2/subscriptions/channels/{channelId}", channel.getId())
                            .header("Authorization", token))
                    .andDo(print())
                    .andDo(document("v2/subscriptions/channels/channelId",
                            fromRequest(),
                            fromResponse(),
                            pathParameters(
                                    parameterWithName("channelId").description("구독을 취소할 채널의 ID")
                            )
                    ));
        }
    }
}