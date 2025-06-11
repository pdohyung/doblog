package project.doblog.application.post.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostSearchServiceRequest {

    private final int page;

    private final int size;

    @Builder
    public PostSearchServiceRequest(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public long getOffset() {
        return (long) (page - 1) * size;
    }
}
