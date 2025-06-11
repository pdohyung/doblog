package project.doblog.application.post.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostEditServiceRequest {

    private final Long postId;

    private final String title;

    private final String content;

    @Builder
    public PostEditServiceRequest(Long postId, String title, String content) {
        this.postId = postId;
        this.title = title;
        this.content = content;
    }
}
