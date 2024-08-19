package com.jeulog.repository;

import com.jeulog.domain.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import java.util.List;

import static com.jeulog.domain.QPost.*;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    @Override
    public List<Post> getList(int page) {
         return queryFactory
                 .selectFrom(post)
                 .limit(10)
                 .offset((page - 1) * 10)
                 .orderBy(post.id.desc())
                 .fetch();
    }
}
