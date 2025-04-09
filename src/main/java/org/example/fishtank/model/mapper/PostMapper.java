package org.example.fishtank.model.mapper;

import org.example.fishtank.model.dto.postDto.CreatePost;
import org.example.fishtank.model.dto.postDto.ResponsePost;
import org.example.fishtank.model.dto.postDto.UpdatePost;
import org.example.fishtank.model.entity.*;

import java.util.Objects;

public class PostMapper {

    public static ResponsePost map(Post post) {
        if (Objects.isNull(post))
            return null;
        return new ResponsePost(
                post.getId(),
                post.getText(),
                post.getFishid().getId());
    }

    public static Post map(CreatePost createPost, Fish fish) {
        if (Objects.isNull(createPost))
            return null;
        Post post = new Post();
        post.setText(createPost.text());
        post.setFishid(fish);
        return post;
    }

    public static void map(UpdatePost updatePost, Post oldPost) {
        if (Objects.nonNull(updatePost.text())) {
            oldPost.setText(updatePost.text());
        }
    }
}
