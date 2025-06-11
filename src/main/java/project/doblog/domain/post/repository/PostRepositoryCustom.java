package project.doblog.domain.post.repository;

import project.doblog.domain.post.Post;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getPosts(int size, long offset);
}
