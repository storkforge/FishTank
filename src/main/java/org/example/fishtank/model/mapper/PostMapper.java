package org.example.fishtank.model.mapper;

import org.example.fishtank.model.dto.postDto.CreatePost;
import org.example.fishtank.model.dto.postDto.ResponsePost;
import org.example.fishtank.model.dto.postDto.UpdatePost;
import org.example.fishtank.model.entity.*;
import org.geolatte.geom.G2D;

import java.util.Objects;

public class PostMapper {

    public static ResponsePost map(Post post) {
        if (Objects.isNull(post)) {
            return null;
        }

        Double longitude = null;
        Double latitude = null;
        if (post.getCoordinate() != null) {
            G2D position = post.getCoordinate().getPosition();
            longitude = position.getLon();
            latitude = position.getLat();
        }

        return new ResponsePost(
                post.getId(),
                post.getText(),
                post.getCityName(),
                longitude,
                latitude,
                post.getFishid().getId()
        );
    }

    public static Post map(CreatePost createPost, Fish fish) {
        if (Objects.isNull(createPost))
            return null;
        Post post = new Post();
        post.setText(createPost.text());
        post.setFishid(fish);
        post.setCityName(createPost.cityName());

        return post;
    }

    public static void map(UpdatePost updatePost, Post oldPost) {
        if (Objects.nonNull(updatePost.text())) {
            oldPost.setText(updatePost.text());
        }
        if (updatePost.cityName() != null){
            oldPost.setCityName(updatePost.cityName());
        }
    }
}
