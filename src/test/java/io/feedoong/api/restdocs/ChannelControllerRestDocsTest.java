package io.feedoong.api.restdocs;

import io.feedoong.api.domain.channel.Channel;
import io.feedoong.api.domain.channel.ChannelRepository;
import io.feedoong.api.domain.user.User;
import io.feedoong.api.domain.user.UserRepository;
import io.feedoong.api.shared.base.BaseRestDocsTest;
import io.feedoong.api.shared.factory.ChannelFactory;
import io.feedoong.api.shared.factory.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;

import static io.feedoong.api.shared.util.ApiDocumentationUtils.fromRequest;
import static io.feedoong.api.shared.util.ApiDocumentationUtils.fromResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@DisplayName("ChannelController REST Docs")
class ChannelControllerRestDocsTest extends BaseRestDocsTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Nested
    @DisplayName("GET /v2/channels/{channelId} - getChannelDetails 메소드")
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
        public void success() throws Exception {
            mockMvc.perform(get("/v2/channels/{channelId}", channel.getId())
                            .header("Authorization", token))
                    .andDo(print())
                    .andDo(document("v2/channels/channelId",
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