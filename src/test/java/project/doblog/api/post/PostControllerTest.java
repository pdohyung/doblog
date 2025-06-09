package project.doblog.api.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import project.doblog.api.ControllerTestSupport;
import project.doblog.api.post.request.PostCreateRequest;
import project.doblog.application.post.request.PostSearchServiceRequest;
import project.doblog.application.post.response.PostResponse;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostControllerTest extends ControllerTestSupport {

    @DisplayName("글 제목은 필수다.")
    @Test
    void writePostWithoutTitle() throws Exception {
        PostCreateRequest request = PostCreateRequest.builder()
                .title(" ")
                .content("내용")
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("400"))
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("message").value("제목을 입력하세요."))
                .andExpect(jsonPath("data").isEmpty())
                .andDo(print());
    }

    @DisplayName("글 내용은 필수다.")
    @Test
    void writePostWithoutContent() throws Exception {
        PostCreateRequest request = PostCreateRequest.builder()
                .title("제목")
                .content(" ")
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("400"))
                .andExpect(jsonPath("status").value("BAD_REQUEST"))
                .andExpect(jsonPath("message").value("내용을 입력하세요."))
                .andExpect(jsonPath("data").isEmpty())
                .andDo(print());
    }

    @DisplayName("글 작성을 요청한다.")
    @Test
    void writePostWithValidRequest() throws Exception {
        PostCreateRequest request = PostCreateRequest.builder()
                .title("제목")
                .content("내용")
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value("200"))
                .andExpect(jsonPath("status").value("OK"))
                .andExpect(jsonPath("message").value("OK"))
                .andExpect(jsonPath("data").isEmpty())
                .andDo(print());
    }

    @DisplayName("글을 단건 조회한다.")
    @Test
    void getPost() throws Exception {
        Long postId = 1L;

        PostResponse response = PostResponse.builder()
                .id(postId)
                .title("제목")
                .content("내용")
                .build();

        when(postService.get(postId)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/posts/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("code").value("200"))
                .andExpect(jsonPath("status").value("OK"))
                .andExpect(jsonPath("message").value("OK"))
                .andExpect(jsonPath("data").exists())
                .andDo(print());
    }

    @DisplayName("1페이지 글을 조회한다.")
    @Test
    void getPosts() throws Exception {
        when(postService.getPosts(any(PostSearchServiceRequest.class))).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/posts?page=1&size=5")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("code").value("200"))
                .andExpect(jsonPath("status").value("OK"))
                .andExpect(jsonPath("message").value("OK"))
                .andExpect(jsonPath("data").isArray())
                .andDo(print());
    }
}
