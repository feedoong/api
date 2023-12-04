package io.feedoong.api.restdocs;

import io.feedoong.api.controller.SubscriptionController;
import io.feedoong.api.domain.channel.dto.ChannelDetailsDTO;
import io.feedoong.api.service.SubscriptionService;
import io.feedoong.api.shared.base.BaseRestDocsTest;
import io.feedoong.api.shared.factory.ChannelFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static io.feedoong.api.shared.util.ApiDocumentationUtils.fromRequest;
import static io.feedoong.api.shared.util.ApiDocumentationUtils.fromResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubscriptionController.class)
@DisplayName("SubscriptionController REST Docs")
class SubscriptionControllerRestDocsTest extends BaseRestDocsTest {
    @MockBean
    private SubscriptionService subscriptionService;

    @Test
    @DisplayName("GET /v2/subscriptions - getSubscriptions")
    public void getSubscriptions() throws Exception {
        Page<ChannelDetailsDTO> page = mockChannelDetailsDTOPage();
        when(subscriptionService.getSubscribedChannels(any(PageRequest.class), any(UserDetails.class)))
                .thenReturn(page);

        ChannelDetailsDTO channelDetails1 = page.getContent().get(0);
        ChannelDetailsDTO channelDetails2 = page.getContent().get(1);

        mockMvc.perform(get("/v2/subscriptions")
                        .header("Authorization", bearerToken)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "createdAt")
                        .param("direction", "desc"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents[0].id").value(channelDetails1.getId()))
                .andExpect(jsonPath("$.contents[0].title").value(channelDetails1.getTitle()))
                .andExpect(jsonPath("$.contents[0].description").value(channelDetails1.getDescription()))
                .andExpect(jsonPath("$.contents[0].url").value(channelDetails1.getUrl()))
                .andExpect(jsonPath("$.contents[0].feedUrl").value(channelDetails1.getFeedUrl()))
                .andExpect(jsonPath("$.contents[0].imageUrl").value(channelDetails1.getImageUrl()))
                .andExpect(jsonPath("$.contents[0].isSubscribed").value(channelDetails1.getIsSubscribed()))
                .andExpect(jsonPath("$.contents[1].id").value(channelDetails2.getId()))
                .andExpect(jsonPath("$.contents[1].title").value(channelDetails2.getTitle()))
                .andExpect(jsonPath("$.contents[1].description").value(channelDetails2.getDescription()))
                .andExpect(jsonPath("$.contents[1].url").value(channelDetails2.getUrl()))
                .andExpect(jsonPath("$.contents[1].feedUrl").value(channelDetails2.getFeedUrl()))
                .andExpect(jsonPath("$.contents[1].imageUrl").value(channelDetails2.getImageUrl()))
                .andExpect(jsonPath("$.contents[1].isSubscribed").value(channelDetails2.getIsSubscribed()))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.hasNext").value(false))
                .andExpect(jsonPath("$.isFirst").value(true))
                .andExpect(jsonPath("$.isLast").value(true))
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

    @Test
    @DisplayName("DELETE /v2/subscriptions/channels/{channelId} - unsubscribe")
    public void unsubscribe() throws Exception {
        mockMvc.perform(delete("/v2/subscriptions/channels/{channelId}", 1L)
                        .header("Authorization", bearerToken))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("v2/subscriptions/channels/channelId",
                        fromRequest(),
                        fromResponse(),
                        pathParameters(
                                parameterWithName("channelId").description("구독을 취소할 채널의 ID")
                        )));
    }

    private Page<ChannelDetailsDTO> mockChannelDetailsDTOPage() {
        List<ChannelDetailsDTO> channelDetailsDTOList = ChannelFactory.mockChannelDetailsDTOList();
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        return new PageImpl<>(channelDetailsDTOList, pageable, channelDetailsDTOList.size());
    }
}