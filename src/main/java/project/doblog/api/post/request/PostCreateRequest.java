package project.doblog.api.post.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import project.doblog.application.post.request.PostCreateServiceRequest;

@Getter
public class PostCreateRequest {

    @NotBlank(message = "제목을 입력하세요.")
    private final String title;

    @NotBlank(message = "내용을 입력하세요.")
    private final String content;

    @Builder
    private PostCreateRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public PostCreateServiceRequest toServiceRequest() {
        return PostCreateServiceRequest.builder()
                .title(this.title)
                .content(this.content)
                .build();
    }
}
