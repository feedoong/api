package io.feedoong.api.restdocs;

import io.feedoong.api.controller.ChannelController;
import io.feedoong.api.domain.channel.dto.ChannelDetailsDTO;
import io.feedoong.api.service.ChannelService;
import io.feedoong.api.shared.mock.ChannelMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static io.feedoong.api.shared.util.ApiDocumentationUtils.fromRequest;
import static io.feedoong.api.shared.util.ApiDocumentationUtils.fromResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureRestDocs
@EnableWebSecurity
@WebMvcTest(ChannelController.class)
@ActiveProfiles("test")
@DisplayName("ChannelController REST Docs")
class ChannelControllerRestDocsTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChannelService channelService;

    @Test
    @WithMockUser
    @DisplayName("GET /v2/channels/{channelId} -  getChannelDetails")
    public void getChannelDetails() throws Exception {
        ChannelDetailsDTO channelDetails = ChannelMock.channelDetailsDTO();
        when(channelService.getChannelDetails(anyLong(), any(UserDetails.class)))
                .thenReturn(channelDetails);

        mockMvc.perform(get("/v2/channels/{channelId}", channelDetails.getId()))
                .andDo(print())
                .andExpect(jsonPath("$.data.id").value(channelDetails.getId()))
                .andExpect(jsonPath("$.data.title").value(channelDetails.getTitle()))
                .andExpect(jsonPath("$.data.description").value(channelDetails.getDescription()))
                .andExpect(jsonPath("$.data.url").value(channelDetails.getUrl()))
                .andExpect(jsonPath("$.data.feedUrl").value(channelDetails.getFeedUrl()))
                .andExpect(jsonPath("$.data.imageUrl").value(channelDetails.getImageUrl()))
                .andExpect(jsonPath("$.data.isSubscribed").value(channelDetails.getIsSubscribed()))
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