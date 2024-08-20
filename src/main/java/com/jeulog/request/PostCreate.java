package com.jeulog.request;

import com.jeulog.exception.InvalidRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@ToString
@Getter @Setter
public class PostCreate {
    @NotBlank(message = "타이틀을 입력해주세요")
    private String title;
    @NotBlank(message = "컨텐트를 입력해주세요")
    private String content;
    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }
    public void validate(PostCreate request) {
        if(request.getTitle().contains("바보")) throw new InvalidRequest();
    }
    // 빌더의 장점
    // 가독성에 좋다 (값 생성에 대한 유연한)
    // 필요한 값만 받을 수 있다. -> 생성자 오버로딩이 필요 X
    // 객체의 불변성 (final 지정 가능)
    // 생성자 주입 실수를 안한다.
}
