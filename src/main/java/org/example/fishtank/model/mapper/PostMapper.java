package org.example.fishtank.model.mapper;

import org.example.fishtank.model.dto.postDto.CreatePost;
import org.example.fishtank.model.dto.postDto.ResponsePost;
import org.example.fishtank.model.dto.postDto.UpdatePost;
import org.example.fishtank.model.entity.*;

public class PostMapper {

    public static ResponsePost map(Post post) {
        if (null == post)
            return null;
        return new ResponsePost(
                post.getId(),
                post.getText(),
                post.getFishid().getId());
    }

    public static Post map(CreatePost createPost, Fish fish) {
        if (null == createPost)
            return null;
        Post post = new Post();
        post.setText(createPost.text());
        post.setFishid(fish);
        return post;
    }

    public static void map(UpdatePost updatePost, Post oldPost) {
        if (updatePost.text() != null) {
            oldPost.setText(updatePost.text());
        }
    }
}
