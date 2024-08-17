package com.jeulog.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;


@ToString
@Getter @Setter
public class PostCreate {
    @NotBlank(message = "타이틀을 입력해주세요")
    private String title;
    @NotBlank(message = "컨텐트를 입력해주세요")
    private String content;
}
