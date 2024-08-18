package com.jeulog.response;

import lombok.Builder;
import lombok.Getter;

/**
 * 서비스 정책 title 길이를 최대 10으로 해라 -> 엔티티 응답 -> 응답용 클래스 분리
 */
@Getter
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    @Builder
    public PostResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title.substring(0, Math.min(title.length(), 10));
        this.content = content;
    }
}
