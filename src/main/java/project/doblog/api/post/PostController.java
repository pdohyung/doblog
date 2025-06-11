package project.doblog.api.post;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.doblog.api.post.request.PostCreateRequest;
import project.doblog.api.post.request.PostEditRequest;
import project.doblog.api.post.request.PostSearchRequest;
import project.doblog.application.post.PostService;
import project.doblog.application.post.response.PostResponse;
import project.doblog.exception.ApiResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ApiResponse<Void> createPost(@RequestBody @Valid PostCreateRequest request) {
        postService.write(request.toServiceRequest());
        return ApiResponse.ok();
    }

    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> getPost(@PathVariable Long postId) {
        PostResponse response = postService.get(postId);
        return ApiResponse.ok(response);
    }

    @GetMapping
    public ApiResponse<List<PostResponse>> getPosts(@ModelAttribute PostSearchRequest request) {
        List<PostResponse> response = postService.getList(request.toServiceRequest());
        return ApiResponse.ok(response);
    }

    @PatchMapping("/{postId}")
    public ApiResponse<Void> editPost(@PathVariable Long postId, @RequestBody @Valid PostEditRequest request) {
        postService.edit(request.toServiceRequest(postId));
        return ApiResponse.ok();
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<Void> deletePost(@PathVariable Long postId) {
        postService.delete(postId);
        return ApiResponse.ok();
    }
}
