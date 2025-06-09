package project.doblog.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.doblog.domain.post.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
