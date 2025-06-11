package project.doblog.api.post.request;

import lombok.Builder;
import lombok.Getter;
import project.doblog.application.post.request.PostSearchServiceRequest;

@Getter
public class PostSearchRequest {

    private static final int MAX_SIZE = 2_000;

    private final Integer page;

    private final Integer size;

    @Builder
    public PostSearchRequest(Integer page, Integer size) {
        this.page = page == null ? 1 : Math.max(1, page);
        this.size = size == null ? 10 : Math.min(size, MAX_SIZE);
    }

    public PostSearchServiceRequest toServiceRequest() {
        return PostSearchServiceRequest.builder()
                .page(this.page)
                .size(this.size)
                .build();
    }
}
