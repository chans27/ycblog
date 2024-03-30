package com.ycblog.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostEdit {

    @NotBlank(message = "Title should not be blank")
    private String title;
    @NotBlank(message = "Content should not be blank")
    private String content;

    @Builder
    public PostEdit(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
