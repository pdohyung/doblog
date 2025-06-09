package project.doblog.api.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import project.doblog.api.ControllerTestSupport;
import project.doblog.api.post.request.PostCreateRequest;

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
                .andExpect(jsonPath("message").value("제목을 입력하세요."))
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
                .andExpect(jsonPath("message").value("내용을 입력하세요."))
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
                .andDo(print());
    }

    @DisplayName("글을 단건 조회한다.")
    @Test
    void getPost() throws Exception {
        Long postId = 1L;

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/posts/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
}
