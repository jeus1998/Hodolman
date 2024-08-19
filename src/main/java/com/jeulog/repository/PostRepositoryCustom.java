package com.jeulog.repository;

import com.jeulog.domain.Post;
import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getList(int page);
}
