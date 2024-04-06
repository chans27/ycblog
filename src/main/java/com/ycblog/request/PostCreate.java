package com.ycblog.request;

import com.ycblog.exception.InvalidRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class PostCreate {

    @NotBlank(message = "Title should not be blank")
    private String title;
    @NotBlank(message = "Content should not be blank")
    private String content;

    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void validate() {
        if(title.toLowerCase().contains("java")) {
            throw new InvalidRequest("title", "'java' could not be included in title");
        }
    }
}
