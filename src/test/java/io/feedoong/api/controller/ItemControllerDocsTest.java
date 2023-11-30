package io.feedoong.api.controller;

import io.feedoong.api.domain.channel.Channel;
import io.feedoong.api.domain.subscription.Subscription;
import io.feedoong.api.domain.user.User;
import io.feedoong.api.domain.item.Item;
import io.feedoong.api.domain.item.ItemRepository;
import io.feedoong.api.domain.channel.ChannelRepository;
import io.feedoong.api.domain.subscription.SubscriptionRepository;
import io.feedoong.api.domain.user.UserRepository;
import io.feedoong.api.global.security.jwt.TokenProvider;
import io.feedoong.api.shared.factory.ChannelFactory;
import io.feedoong.api.shared.factory.ItemFactory;
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
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static io.feedoong.api.shared.util.ApiDocumentationUtils.fromRequest;
import static io.feedoong.api.shared.util.ApiDocumentationUtils.fromResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureRestDocs
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@DisplayName("ItemController 클래스 REST Documentation")
class ItemControllerDocsTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Nested
    @DisplayName("GET /v2/items - getItems 메소드")
    class GetItems {
        private String token;

        @BeforeEach
        void prepare() {
            User user = UserFactory.create();
            userRepository.save(user);
            token = tokenProvider.create(user);

            Channel channel = ChannelFactory.create();
            channelRepository.save(channel);

            Item item = ItemFactory.create(channel);
            itemRepository.save(item);

            Subscription subscription = SubscriptionFactory.create(channel, user);
            subscriptionRepository.save(subscription);
        }

        @Test
        @DisplayName("아이템 조회 성공")
        public void getItems_Success() throws Exception {
            mockMvc.perform(get("/v2/items")
                            .header("Authorization", token)
                            .param("page", "0")
                            .param("size", "10")
                            .param("sort", "publishedAt")
                            .param("direction", "desc"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(jsonPath("$.totalElements").value(1))
                    .andExpect(jsonPath("$.page").value(0))
                    .andExpect(jsonPath("$.size").value(10))
                    .andExpect(jsonPath("$.hasNext").value(false))
                    .andExpect(jsonPath("$.isFirst").value(true))
                    .andExpect(jsonPath("$.isLast").value(true))
                    .andExpect(jsonPath("$.contents").isArray())
                    .andDo(document("v2/items",
                            fromRequest(),
                            fromResponse(),
                            RequestDocumentation.queryParameters(
                                    parameterWithName("page").description("페이지 번호 (0부터 시작)"),
                                    parameterWithName("size").description("한 페이지 당 반환되는 항목의 수"),
                                    parameterWithName("sort").description("정렬 기준이 되는 필드 (예: 'publishedAt')"),
                                    parameterWithName("direction").description("정렬 방향 (asc 또는 desc)")
                            ),
                            PayloadDocumentation.responseFields(
                                    fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수"),
                                    fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("총 요소 수"),
                                    fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                                    fieldWithPath("size").type(JsonFieldType.NUMBER).description("페이지 당 항목 수"),
                                    fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부"),
                                    fieldWithPath("isFirst").type(JsonFieldType.BOOLEAN).description("현재 페이지가 첫 번째 페이지인지 여부"),
                                    fieldWithPath("isLast").type(JsonFieldType.BOOLEAN).description("현재 페이지가 마지막 페이지인지 여부"),
                                    fieldWithPath("contents").type(JsonFieldType.ARRAY).description("현재 페이지의 콘텐츠 목록 (ChannelItemDTO)"),
                                    fieldWithPath("contents[].id").type(JsonFieldType.NUMBER).description("아이템 ID"),
                                    fieldWithPath("contents[].title").type(JsonFieldType.STRING).description("아이템 제목"),
                                    fieldWithPath("contents[].description").type(JsonFieldType.STRING).description("아이템 설명"),
                                    fieldWithPath("contents[].link").type(JsonFieldType.STRING).description("아이템 URL"),
                                    fieldWithPath("contents[].guid").type(JsonFieldType.STRING).description("아이템 GUID"),
                                    fieldWithPath("contents[].publishedAt").type(JsonFieldType.STRING).description("아이템 퍼블리싱 시점"),
                                    fieldWithPath("contents[].imageUrl").type(JsonFieldType.STRING).description("아이템 이미지 URL"),
                                    fieldWithPath("contents[].isLiked").type(JsonFieldType.BOOLEAN).description("좋아요 여부"),
                                    fieldWithPath("contents[].isViewed").type(JsonFieldType.BOOLEAN).description("조회 여부"),
                                    fieldWithPath("contents[].channelId").type(JsonFieldType.NUMBER).description("채널 ID"),
                                    fieldWithPath("contents[].channelTitle").type(JsonFieldType.STRING).description("채널 제목"),
                                    fieldWithPath("contents[].channelImageUrl").type(JsonFieldType.STRING).description("채널 이미지 URL")
                            )
                    ));
        }
    }
}