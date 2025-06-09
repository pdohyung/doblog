package project.doblog.application.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.doblog.application.post.request.PostCreateServiceRequest;
import project.doblog.application.post.request.PostSearchServiceRequest;
import project.doblog.application.post.response.PostResponse;
import project.doblog.domain.post.Post;
import project.doblog.domain.post.repository.PostRepository;
import project.doblog.exception.error.PostNotFoundException;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public void write(PostCreateServiceRequest request) {
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        postRepository.save(post);
    }

    public PostResponse get(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        return new PostResponse(post);
    }

    public List<PostResponse> getPosts(PostSearchServiceRequest request) {
        return postRepository.getPosts(request.getSize(), request.getOffset()).stream()
                .map(PostResponse::new)
                .toList();
    }
}
