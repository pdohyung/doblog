package project.doblog.api.post;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.doblog.api.post.request.PostCreateRequest;
import project.doblog.application.post.PostService;
import project.doblog.domain.post.Post;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public void createPost(@RequestBody @Valid PostCreateRequest request) {
        postService.write(request.toServiceRequest());
    }

    @GetMapping("/{postId}")
    public Post getPost(@PathVariable Long postId) {
        return postService.get(postId);
    }
}
