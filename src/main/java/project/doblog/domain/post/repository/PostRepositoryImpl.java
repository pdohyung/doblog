package project.doblog.domain.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import project.doblog.domain.post.Post;

import java.util.List;

import static project.doblog.domain.post.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getPosts(int size, long offset) {
        return jpaQueryFactory.selectFrom(post)
                .limit(size)
                .offset(offset)
                .orderBy(post.id.desc())
                .fetch();
    }
}
