package org.example.fishtank.controller;

import org.example.fishtank.model.dto.fishDto.ResponseFish;
import org.example.fishtank.model.dto.postDto.ResponsePost;
import org.example.fishtank.service.FishService;
import org.example.fishtank.service.PostService;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PostResolver {

    //Klar!

    private PostService postService;
    private FishService fishService;

    public PostResolver(FishService fishService, PostService postService) {
        this.fishService = fishService;
        this.postService = postService;
    }

    @QueryMapping
    public List<ResponsePost> getAllPost() {
        return postService.getAllPost();
    }

    @SchemaMapping(typeName = "ResponsePost", field = "fish")
    public ResponseFish getPostsForFish(ResponsePost post) {
        return fishService.findById(post.fishId());
    }

}
