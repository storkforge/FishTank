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
public class FishResolver {

    private FishService fishService;
    private PostService postService;

    public FishResolver(FishService fishService, PostService postService) {
        this.fishService = fishService;
        this.postService = postService;
    }

    @QueryMapping
    public List<ResponseFish> getAllFish() {
        return fishService.getAllFish();
    }

    @QueryMapping
    public ResponseFish getFishById(Integer id) {
        return fishService.findById(id);
    }


//    @SchemaMapping(typeName = "ResponseFish", field = "posts")
//    public List<ResponsePost> getPostsForFish(ResponseFish fish) {
//        return postService.findByFishId(fish);
//    }

    @QueryMapping
    public List<ResponsePost> getAllPostByFishId(@Argument Integer id) {
        return postService.findByFishId(id);
    }



}
