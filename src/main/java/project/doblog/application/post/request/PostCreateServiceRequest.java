package project.doblog.application.post.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostCreateServiceRequest {

    private final String title;

    private final String content;

    @Builder
    public PostCreateServiceRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
