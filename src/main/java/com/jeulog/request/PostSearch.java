package com.jeulog.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static java.lang.Math.*;

@Getter
@Setter
public class PostSearch {
    private static final int MAX_SIZE = 2000;
    private Integer page;
    private Integer size;
    // 생성자에서 null 체크를 하도록 변경
    @Builder
    public PostSearch(Integer page, Integer size) {
        this.page = page == null ? 1 : page;
        this.size = size == null ? 10 : size;
    }

    public long getOffset(){
        return (long) (max(1, page) - 1) * min(size, MAX_SIZE);
    }
}
