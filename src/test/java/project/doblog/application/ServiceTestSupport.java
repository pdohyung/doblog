package project.doblog.application;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import project.doblog.application.post.PostService;
import project.doblog.domain.post.repository.PostRepository;

@SpringBootTest
public abstract class ServiceTestSupport {

    @Autowired
    protected PostRepository postRepository;

    @Autowired
    protected PostService postService;

    @AfterEach
    void tearDown() {
        postRepository.deleteAllInBatch();
    }
}
