package io.feedoong.api.restdocs;

import io.feedoong.api.controller.ItemController;
import io.feedoong.api.domain.dto.ChannelItemDTO;
import io.feedoong.api.service.ItemService;
import io.feedoong.api.shared.base.BaseRestDocsTest;
import io.feedoong.api.shared.factory.ItemFactory;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ItemController.class)
@DisplayName("ItemController REST Docs")
class ItemControllerRestDocsTest extends BaseRestDocsTest {
    @MockBean
    private ItemService itemService;

    @Test
    @DisplayName("GET /v2/items - getItems")
    public void getItems() throws Exception {
        Page<ChannelItemDTO> page = mockChannelItemDTOPage();
        when(itemService.getItems(any(PageRequest.class), any(UserDetails.class)))
                .thenReturn(page);

        List<ChannelItemDTO> contents = page.getContent();

        mockMvc.perform(get("/v2/items")
                        .header("Authorization", bearerToken)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "publishedAt")
                        .param("direction", "desc"))
                .andDo(print())
                .andExpect(jsonPath("$.contents[0].id").value(contents.get(0).getId()))
                .andExpect(jsonPath("$.contents[0].title").value(contents.get(0).getTitle()))
                .andExpect(jsonPath("$.contents[0].description").value(contents.get(0).getDescription()))
                .andExpect(jsonPath("$.contents[0].link").value(contents.get(0).getLink()))
                .andExpect(jsonPath("$.contents[0].guid").value(contents.get(0).getGuid()))
                .andExpect(jsonPath("$.contents[0].publishedAt").value(contents.get(0).getPublishedAt().toString()))
                .andExpect(jsonPath("$.contents[0].imageUrl").value(contents.get(0).getImageUrl()))
                .andExpect(jsonPath("$.contents[0].isLiked").value(contents.get(0).getIsLiked()))
                .andExpect(jsonPath("$.contents[0].isViewed").value(contents.get(0).getIsViewed()))
                .andExpect(jsonPath("$.contents[0].channelId").value(contents.get(0).getChannelId()))
                .andExpect(jsonPath("$.contents[0].channelTitle").value(contents.get(0).getChannelTitle()))
                .andExpect(jsonPath("$.contents[0].channelImageUrl").value(contents.get(0).getChannelImageUrl()))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.hasNext").value(false))
                .andExpect(jsonPath("$.isFirst").value(true))
                .andExpect(jsonPath("$.isLast").value(true))
                .andDo(document("v2/items",
                        fromRequest(),
                        fromResponse(),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호 (0부터 시작)"),
                                parameterWithName("size").description("한 페이지 당 반환되는 항목의 수"),
                                parameterWithName("sort").description("정렬 기준이 되는 필드 (예: 'publishedAt')"),
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

    @Test
    @DisplayName("GET /v2/items/channel/{channelId} - getChannelItems")
    public void GetChannelItems() throws Exception {
        Page<ChannelItemDTO> page = mockChannelItemDTOPage();
        when(itemService.getItemsByChannel(any(PageRequest.class), any(UserDetails.class), anyLong()))
                .thenReturn(page);

        List<ChannelItemDTO> contents = page.getContent();

        mockMvc.perform(get("/v2/items/channel/{channelId}", 1L)
                        .header("Authorization", bearerToken)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "publishedAt")
                        .param("direction", "desc"))
                .andDo(print())
                .andExpect(jsonPath("$.contents[0].id").value(contents.get(0).getId()))
                .andExpect(jsonPath("$.contents[0].title").value(contents.get(0).getTitle()))
                .andExpect(jsonPath("$.contents[0].description").value(contents.get(0).getDescription()))
                .andExpect(jsonPath("$.contents[0].link").value(contents.get(0).getLink()))
                .andExpect(jsonPath("$.contents[0].guid").value(contents.get(0).getGuid()))
                .andExpect(jsonPath("$.contents[0].publishedAt").value(contents.get(0).getPublishedAt().toString()))
                .andExpect(jsonPath("$.contents[0].imageUrl").value(contents.get(0).getImageUrl()))
                .andExpect(jsonPath("$.contents[0].isLiked").value(contents.get(0).getIsLiked()))
                .andExpect(jsonPath("$.contents[0].isViewed").value(contents.get(0).getIsViewed()))
                .andExpect(jsonPath("$.contents[0].channelId").value(contents.get(0).getChannelId()))
                .andExpect(jsonPath("$.contents[0].channelTitle").value(contents.get(0).getChannelTitle()))
                .andExpect(jsonPath("$.contents[0].channelImageUrl").value(contents.get(0).getChannelImageUrl()))
                .andExpect(jsonPath("$.contents[0].id").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.hasNext").value(false))
                .andExpect(jsonPath("$.isFirst").value(true))
                .andExpect(jsonPath("$.isLast").value(true))
                .andDo(document("v2/items/channel/channelId",
                        fromRequest(),
                        fromResponse(),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호 (0부터 시작)"),
                                parameterWithName("size").description("한 페이지 당 반환되는 항목의 수"),
                                parameterWithName("sort").description("정렬 기준이 되는 필드 (예: 'publishedAt')"),
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
                                fieldWithPath("contents").type(JsonFieldType.ARRAY).description("현재 페이지의 콘텐츠 목록 (ChannelItemDTO)"),
                                fieldWithPath("contents[].id").type(JsonFieldType.NUMBER).description("아이템 ID"),
                                fieldWithPath("contents[].title").type(JsonFieldType.STRING).description("아이템 제목"),
                                fieldWithPath("contents[].description").type(JsonFieldType.STRING).description("아이템 설명"),
                                fieldWithPath("contents[].link").type(JsonFieldType.STRING).description("아이템 URL"),
                                fieldWithPath("contents[].guid").type(JsonFieldType.STRING).description("아이템 GUID"),
                                fieldWithPath("contents[].publishedAt").type(JsonFieldType.STRING).description("아이템 퍼블리싱 시점"),
                                fieldWithPath("contents[].imageUrl").type(JsonFieldType.STRING).description("아이템 이미지 URL"),
                                fieldWithPath("contents[].isLiked").type(JsonFieldType.BOOLEAN).description("좋아요 여부, 로그인하지 않은 사용자의 경우 'false' 로 고정"),
                                fieldWithPath("contents[].isViewed").type(JsonFieldType.BOOLEAN).description("조회 여부, 로그인하지 않은 사용자의 경우 'false'로 고정"),
                                fieldWithPath("contents[].channelId").type(JsonFieldType.NUMBER).description("채널 ID"),
                                fieldWithPath("contents[].channelTitle").type(JsonFieldType.STRING).description("채널 제목"),
                                fieldWithPath("contents[].channelImageUrl").type(JsonFieldType.STRING).description("채널 이미지 URL")
                        )
                ));
    }

    private Page<ChannelItemDTO> mockChannelItemDTOPage() {
        List<ChannelItemDTO> channelItemDTOs = ItemFactory.mockChannelItemDTOs();
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("publishedAt").descending());

        return new PageImpl<>(channelItemDTOs, pageable, channelItemDTOs.size());
    }
}