package io.feedoong.api.controller;

import io.feedoong.api.domain.channel.Channel;
import io.feedoong.api.domain.channel.ChannelRepository;
import io.feedoong.api.domain.user.User;
import io.feedoong.api.domain.user.UserRepository;
import io.feedoong.api.global.security.jwt.TokenProvider;
import io.feedoong.api.shared.factory.ChannelFactory;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static io.feedoong.api.shared.util.ApiDocumentationUtils.fromRequest;
import static io.feedoong.api.shared.util.ApiDocumentationUtils.fromResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Transactional
@AutoConfigureRestDocs
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@DisplayName("ChannelController 클래스 REST Documentations")
class ChannelControllerDocsTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Nested
    @DisplayName("getChannelDetails 메소드")
    class GetChannelDetails {
        private String token;
        private Channel channel;

        @BeforeEach
        void prepare() {
            User user = UserFactory.create();
            userRepository.save(user);
            token = tokenProvider.create(user);

            channel = ChannelFactory.create();
            channelRepository.save(channel);
        }

        @Test
        @DisplayName("로그인 사용자의 채널 조회 성공")
        public void getChannelDetails_Success() throws Exception {
            mockMvc.perform(get("/v2/channels/{channelId}", channel.getId())
                            .header("Authorization", token))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$.data.id").value(channel.getId()))
                    .andExpect(jsonPath("$.data.title").value(channel.getTitle()))
                    .andExpect(jsonPath("$.data.description").value(channel.getDescription()))
                    .andExpect(jsonPath("$.data.url").value(channel.getUrl()))
                    .andExpect(jsonPath("$.data.feedUrl").value(channel.getFeedUrl()))
                    .andExpect(jsonPath("$.data.imageUrl").value(channel.getImageUrl()))
                    .andExpect(jsonPath("$.data.isSubscribed").value(false))
                    .andDo(document("v2/channels/channelId/login",
                            fromRequest(),
                            fromResponse(),
                            PayloadDocumentation.responseFields(
                                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("채널 정보 (ChannelDetailsDTO)"),
                                    fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("채널 ID"),
                                    fieldWithPath("data.title").type(JsonFieldType.STRING).description("채널 제목"),
                                    fieldWithPath("data.description").type(JsonFieldType.STRING).description("채널 설명"),
                                    fieldWithPath("data.url").type(JsonFieldType.STRING).description("채널 URL"),
                                    fieldWithPath("data.feedUrl").type(JsonFieldType.STRING).description("채널 피드 URL"),
                                    fieldWithPath("data.imageUrl").type(JsonFieldType.STRING).description("채널 이미지 URL"),
                                    fieldWithPath("data.isSubscribed").type(JsonFieldType.BOOLEAN).description("구독 여부, 로그인하지 않은 사용자의 경우 'false'로 고정")
                            )
                    ));
        }
    }
}