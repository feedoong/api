package io.feedoong.api.restdocs;

import io.feedoong.api.controller.ViewController;
import io.feedoong.api.service.ViewService;
import io.feedoong.api.service.dto.ViewedItemDTO;
import io.feedoong.api.shared.base.BaseRestDocsTest;
import io.feedoong.api.shared.factory.ViewFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.userdetails.UserDetails;

import static io.feedoong.api.shared.util.ApiDocumentationUtils.fromRequest;
import static io.feedoong.api.shared.util.ApiDocumentationUtils.fromResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ViewController.class)
@DisplayName("ViewController REST Docs")
class ViewControllerRestDocsTest extends BaseRestDocsTest {
    @MockBean
    private ViewService viewService;

    @Test
    @DisplayName("POST /v2/views/item/{itemId} - viewItem")
    public void viewItem() throws Exception {
        ViewedItemDTO mockViewedItemDTO = ViewFactory.viewedItemDTO();
        when(viewService.viewItem(any(UserDetails.class), anyLong()))
                .thenReturn(mockViewedItemDTO);

        mockMvc.perform(post("/v2/views/item/{itemId}", mockViewedItemDTO.getItemId())
                        .header("Authorization", bearerToken))
                .andDo(print())
                .andExpect(jsonPath("$.id").value(mockViewedItemDTO.getItemId()))
                .andExpect(jsonPath("$.isViewed").value(mockViewedItemDTO.getIsViewed()))
                .andDo(document("v2/views/item/itemId",
                        fromRequest(),
                        fromResponse(),
                        pathParameters(
                                parameterWithName("itemId").description("읽음 처리할 아이템의 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("아이템 ID"),
                                fieldWithPath("isViewed").type(JsonFieldType.BOOLEAN).description("읽음 여부")
                        )
                ));
    }
}