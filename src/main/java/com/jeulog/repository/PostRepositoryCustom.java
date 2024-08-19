package com.jeulog.repository;

import com.jeulog.domain.Post;
import com.jeulog.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getList(PostSearch postSearch);
}
