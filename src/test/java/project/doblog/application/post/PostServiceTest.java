package project.doblog.application.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.doblog.application.ServiceTestSupport;
import project.doblog.application.post.request.PostCreateServiceRequest;
import project.doblog.application.post.request.PostEditServiceRequest;
import project.doblog.application.post.request.PostSearchServiceRequest;
import project.doblog.application.post.response.PostResponse;
import project.doblog.domain.post.Post;
import project.doblog.exception.error.PostNotFoundException;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @DisplayName("존재하지 않는 글을 단건 조회하면 예외가 발생한다.")
    @Test
    void getPostWithInvalidId() {
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        postRepository.save(post);

        assertThatThrownBy(() -> postService.get(post.getId() + 1L))
                .isInstanceOf(PostNotFoundException.class);
    }

    @DisplayName("글을 단건 조회한다.")
    @Test
    void getPost() {
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        postRepository.save(post);

        PostResponse findPost = postService.get(post.getId());

        assertThat(findPost.getTitle()).isEqualTo("제목");
        assertThat(findPost.getContent()).isEqualTo("내용");
    }

    @DisplayName("1페이지 글을 조회한다.")
    @Test
    void getFirstPagePosts() {
        List<Post> requests = IntStream.range(1, 21)
                .mapToObj(i -> Post.builder()
                        .title("제목" + i)
                        .content("내용" + i)
                        .build())
                .toList();

        postRepository.saveAll(requests);

        PostSearchServiceRequest request = PostSearchServiceRequest.builder()
                .page(1)
                .size(5)
                .build();

        List<PostResponse> posts = postService.getList(request);

        assertThat(posts).hasSize(5)
                .extracting("title", "content")
                .containsExactly(
                        tuple("제목20", "내용20"),
                        tuple("제목19", "내용19"),
                        tuple("제목18", "내용18"),
                        tuple("제목17", "내용17"),
                        tuple("제목16", "내용16")
                );
    }

    @DisplayName("존재하지 않는 글을 수정하면 예외가 발생한다.")
    @Test
    void editPostWithInvalidId() {
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        postRepository.save(post);

        PostEditServiceRequest request = PostEditServiceRequest.builder()
                .postId(post.getId() + 1L)
                .title("수정한 제목")
                .content("수정한 내용")
                .build();

        assertThatThrownBy(() -> postService.edit(request))
                .isInstanceOf(PostNotFoundException.class);
    }

    @DisplayName("글을 수정한다.")
    @Test
    void editPost() {
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        postRepository.save(post);

        PostEditServiceRequest request = PostEditServiceRequest.builder()
                .postId(post.getId())
                .title("수정한 제목")
                .content("수정한 내용")
                .build();

        postService.edit(request);

        List<Post> posts = postRepository.findAll();

        assertThat(posts).hasSize(1)
                .extracting("title", "content")
                .containsExactly(tuple("수정한 제목", "수정한 내용"));
    }

    @DisplayName("존재하지 않는 글을 삭제하면 예외가 발생한다.")
    @Test
    void deletePostWithInvalidId() {
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        postRepository.save(post);

        assertThatThrownBy(() -> postService.delete(post.getId() + 1))
                .isInstanceOf(PostNotFoundException.class);
    }

    @DisplayName("글을 삭제한다.")
    @Test
    void deletePost() {
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        postRepository.save(post);

        postService.delete(post.getId());

        List<Post> posts = postRepository.findAll();

        assertThat(posts).isEmpty();
    }
}
