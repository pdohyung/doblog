package project.doblog.application.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.doblog.application.ServiceTestSupport;
import project.doblog.application.post.request.PostCreateServiceRequest;
import project.doblog.domain.post.Post;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

class PostServiceTest extends ServiceTestSupport {

    @DisplayName("글을 등록한다.")
    @Test
    void writePost() {
        PostCreateServiceRequest request = PostCreateServiceRequest.builder()
                .title("제목")
                .content("내용")
                .build();

        postService.write(request);

        List<Post> posts = postRepository.findAll();

        assertThat(posts).hasSize(1)
                .extracting("title", "content")
                .containsExactly(tuple("제목", "내용"));
    }

    @DisplayName("글을 단건 조회한다.")
    @Test
    void getPost() {
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        postRepository.save(post);

        Post findPost = postService.get(post.getId());

        assertThat(findPost.getTitle()).isEqualTo("제목");
        assertThat(findPost.getContent()).isEqualTo("내용");
    }
}
