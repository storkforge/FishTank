package org.example.fishtank.controller.resolver;

import org.example.fishtank.model.dto.fishDto.CreateFish;
import org.example.fishtank.model.dto.fishDto.ResponseFish;
import org.example.fishtank.model.dto.postDto.ResponsePost;
import org.example.fishtank.model.entity.Fish;
import org.example.fishtank.service.FishService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
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

    @MutationMapping
    public ResponseFish addFish(
            @Argument String name,
            @Argument String species,
            @Argument String description,
            @Argument String waterType,
            @Argument String sex,
            @Argument String appUser,
            @Argument String image) {
        CreateFish createFish = new CreateFish(name, species, description, waterType, sex, appUser, image);
        fishService.save(createFish);
        ResponseFish savedFish = fishService.findFishByName(name);
        return savedFish;
    }

    @SchemaMapping(typeName = "ResponsePost", field = "fish")
    public ResponseFish getPostsForFish(ResponsePost post) {
        return fishService.findById(post.fishId());
    }

}
