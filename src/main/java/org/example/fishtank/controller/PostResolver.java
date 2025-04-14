package org.example.fishtank.controller;

import org.example.fishtank.model.dto.fishDto.ResponseFish;
import org.example.fishtank.model.dto.postDto.ResponsePost;
import org.example.fishtank.service.FishService;
import org.example.fishtank.service.PostService;
import org.springframework.graphql.data.method.annotation.Argument;
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

    @SchemaMapping(typeName = "ResponseFish", field = "posts")
    public List<ResponsePost> getPostsForFish(ResponseFish fish) {
        return postService.findByFishId(fish.id());
    }

    @QueryMapping
    public List<ResponsePost> getAllPostByFishId(@Argument Integer id) {
        return postService.findByFishId(id);
    }
}
