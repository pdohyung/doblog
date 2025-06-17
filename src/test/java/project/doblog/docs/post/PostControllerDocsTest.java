package project.doblog.docs.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import project.doblog.api.post.PostController;
import project.doblog.api.post.request.PostCreateRequest;
import project.doblog.api.post.request.PostEditRequest;
import project.doblog.application.post.PostService;
import project.doblog.application.post.request.PostSearchServiceRequest;
import project.doblog.application.post.response.PostResponse;
import project.doblog.docs.RestDocsSupport;

import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostControllerDocsTest extends RestDocsSupport {

    private final PostService postService = mock(PostService.class);

    @Override
    protected Object initController() {
        return new PostController(postService);
    }

    @DisplayName("글 작성 API")
    @Test
    void writePost() throws Exception {
        PostCreateRequest request = PostCreateRequest.builder()
                .title("제목")
                .content("내용")
                .build();

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-write",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("글 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("글 내용")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태 "),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.NULL)
                                        .description("응답 데이터 (NULL)")
                        )
                ));
    }

    @DisplayName("글을 단건 조회 API")
    @Test
    void getPost() throws Exception {
        Long postId = 1L;

        PostResponse response = PostResponse.builder()
                .id(postId)
                .title("제목")
                .content("내용")
                .build();

        given(postService.get(anyLong())).willReturn(response);

        mockMvc.perform(get("/posts/{postId}", postId))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postId")
                                        .description("조회할 글 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태 "),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("글 ID"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING)
                                        .description("글 제목"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING)
                                        .description("글 내용")
                        )
                ));
    }


    @DisplayName("글 페이지 조회 API")
    @Test
    void getPosts() throws Exception {
        List<PostResponse> response = IntStream.range(1, 6)
                .mapToObj(i -> PostResponse.builder()
                        .id((long) i)
                        .title("제목" + i)
                        .content("내용" + i)
                        .build())
                .toList();

        given(postService.getList(any(PostSearchServiceRequest.class))).willReturn(response);

        mockMvc.perform(get("/posts")
                        .param("page", "1")
                        .param("size", "5")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-getList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("page")
                                        .description("페이지 번호"),
                                parameterWithName("size")
                                        .description("페이지 사이즈")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태 "),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY)
                                        .description("응답 데이터"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER)
                                        .description("글 ID"),
                                fieldWithPath("data[].title").type(JsonFieldType.STRING)
                                        .description("글 제목"),
                                fieldWithPath("data[].content").type(JsonFieldType.STRING)
                                        .description("글 내용")
                        )
                ));
    }

    @DisplayName("글 수정 API")
    @Test
    void editPost() throws Exception {
        PostEditRequest request = PostEditRequest.builder()
                .title("제목")
                .content("내용")
                .build();

        mockMvc.perform(patch("/posts/{postId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-edit",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postId")
                                        .description("수정할 글 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("글 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("글 내용")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태 "),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.NULL)
                                        .description("응답 데이터 (NULL)")
                        )
                ));
    }

    @DisplayName("글 삭제 API")
    @Test
    void deletePost() throws Exception {
        mockMvc.perform(delete("/posts/{postId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-delete",
                        pathParameters(
                                parameterWithName("postId")
                                        .description("삭제할 글 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태 "),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.NULL)
                                        .description("응답 데이터 (NULL)")
                        )
                ));
    }
}
