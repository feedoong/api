package io.feedoong.api.restdocs;

import io.feedoong.api.controller.LikeController;
import io.feedoong.api.service.LikeService;
import io.feedoong.api.service.dto.LikeItemDTO;
import io.feedoong.api.shared.base.BaseRestDocsTest;
import io.feedoong.api.shared.factory.LikeFactory;
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
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LikeController.class)
@DisplayName("LikeController REST Docs")
public class LikeControllerRestDocsTest extends BaseRestDocsTest {
    @MockBean
    private LikeService likeService;

    @Test
    @DisplayName("POST /v2/likes/item/{itemId} - like")
    public void like() throws Exception {
        LikeItemDTO likeItemDTO = LikeFactory.createLikeItemDTO();
        when(likeService.like(any(UserDetails.class), anyLong()))
                .thenReturn(likeItemDTO);

        mockMvc.perform(post("/v2/likes/item/{itemId}", likeItemDTO.getItemId())
                        .header("Authorization", bearerToken))
                .andDo(print())
                .andExpect(jsonPath("$.itemId").value(likeItemDTO.getItemId()))
                .andExpect(jsonPath("$.isLiked").value(likeItemDTO.getIsLiked()))
                .andExpect(status().isOk())
                .andDo(document("v2/likes/item/itemId",
                        fromRequest(),
                        fromResponse(),
                        pathParameters(
                                parameterWithName("itemId").description("좋아요할 아이템 ID")
                        ),
                        responseFields(
                                fieldWithPath("itemId").type(JsonFieldType.NUMBER).description("아이템 ID"),
                                fieldWithPath("isLiked").type(JsonFieldType.BOOLEAN).description("좋아요 여부")
                        )
                ));
    }
}
