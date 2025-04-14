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

    public FishResolver(FishService fishService) {
        this.fishService = fishService;
    }

    @QueryMapping
    public List<ResponseFish> getAllFish() {
        return fishService.getAllFish();
    }

    @QueryMapping
    public ResponseFish getFishById(@Argument Integer id) {
        return fishService.findById(id);
    }

    @SchemaMapping(typeName = "ResponsePost", field = "fish")
    public ResponseFish getPostsForFish(ResponsePost post) {
        return fishService.findById(post.fishId());
    }


}
