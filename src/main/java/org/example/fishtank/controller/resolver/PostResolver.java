package org.example.fishtank.controller.resolver;

import org.example.fishtank.model.dto.fishDto.CreateFish;
import org.example.fishtank.model.dto.fishDto.ResponseFish;
import org.example.fishtank.model.dto.postDto.CreatePost;
import org.example.fishtank.model.dto.postDto.ResponsePost;
import org.example.fishtank.model.dto.postDto.UpdatePost;
import org.example.fishtank.model.entity.Post;
import org.example.fishtank.service.PostService;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;


import java.util.List;

@Controller
public class PostResolver {

    private PostService postService;

    public PostResolver(PostService postService) {
        this.postService = postService;
    }

    @QueryMapping
    public List<ResponsePost> getAllPost() {
        return postService.getAllPost();
    }

    @QueryMapping
    public List<ResponsePost> getAllPostByFishId(@Argument Integer id) {
        return postService.findByFishId(id);
    }

    @MutationMapping
    public ResponsePost addPost(
            @Argument String text,
            @Argument String cityName,
            @Argument Integer fishId) {
        CreatePost createPost = new CreatePost(text, cityName, fishId);
        Post savedPost = postService.saveAndReturn(createPost);

        Point<G2D> coord = savedPost.getCoordinate();

        return new ResponsePost(
                savedPost.getId(),
                savedPost.getText(),
                savedPost.getCityName(),
                coord.getPosition().getLon(),
                coord.getPosition().getLat(),
                savedPost.getFishid().getId()
        );
    }

    @MutationMapping
    public ResponsePost updatePost(
            @Argument String text,
            @Argument String cityName,
            @Argument Integer id) {
        UpdatePost updatePost = new UpdatePost(text, cityName);
        postService.update(id, updatePost);

        return postService.findById(id);
    }

    @SchemaMapping(typeName = "ResponseFish", field = "posts")
    public List<ResponsePost> getPostsForFish(ResponseFish fish) {
        return postService.findByFishId(fish.id());
    }
}
