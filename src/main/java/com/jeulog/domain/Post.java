package com.jeulog.domain;

import com.jeulog.request.PostEdit;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Lob
    private String content;
    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }
    public void update(PostEdit postEdit){
        this.title = postEdit.getTitle();
        this.content = postEdit.getContent();
    }
    public PostEditor.PostEditorBuilder toEditor(){
         return PostEditor.builder()
                .title(title)
                .content(content);
    }
    public void edit(PostEditor postEditor){
        title = postEditor.getTitle();
        content = postEditor.getContent();
    }
}
