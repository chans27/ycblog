package com.ycblog.repository;

import com.ycblog.domain.Post;
import com.ycblog.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
