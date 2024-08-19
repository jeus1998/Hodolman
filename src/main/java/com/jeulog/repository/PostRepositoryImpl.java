package com.jeulog.repository;

import com.jeulog.domain.Post;
import com.jeulog.request.PostSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import java.util.List;

import static com.jeulog.domain.QPost.*;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    @Override
    public List<Post> getList(PostSearch postSearch) {
         return queryFactory
                 .selectFrom(post)
                 .limit(postSearch.getSize())
                 .offset(postSearch.getOffset())
                 .orderBy(post.id.desc())
                 .fetch();
    }
}
