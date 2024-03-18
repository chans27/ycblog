package com.ycblog.request;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class PostCreate {

    public String title;
    public String content;
}
