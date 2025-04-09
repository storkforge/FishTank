package org.example.fishtank.model.mapper;

import org.example.fishtank.model.dto.fishDto.CreateFish;
import org.example.fishtank.model.dto.fishDto.ResponseFish;
import org.example.fishtank.model.dto.fishDto.UpdateFish;
import org.example.fishtank.model.entity.AppUser;
import org.example.fishtank.model.entity.Fish;
import org.example.fishtank.model.entity.Sex;
import org.example.fishtank.model.entity.WaterType;

import java.util.Objects;

public class FishMapper {

    public static ResponseFish map(Fish fish) {
        if (Objects.isNull(fish))
            return null;
        return new ResponseFish(
                fish.getId(),
                fish.getName(),
                fish.getSpecies(),
                fish.getDescription(),
                fish.getWaterType().getName(),
                fish.getSex().getName(),
                fish.getAppUser().getName(),
                fish.getImage());
    }

    public static Fish map(CreateFish createFish, WaterType waterType, Sex sex, AppUser appUser) {
        if (Objects.isNull(createFish))
            return null;
        Fish fish = new Fish();
        fish.setName(createFish.name());
        fish.setSpecies(createFish.species());
        fish.setDescription(createFish.description());
        fish.setWaterType(waterType);
        fish.setSex(sex);
        fish.setAppUser(appUser);
        fish.setImage(createFish.image());
        return fish;
    }

    public static void map(UpdateFish updateFish, Fish oldFish) {
        if (Objects.nonNull(updateFish.name())) {
            oldFish.setName(updateFish.name());
        }
        if (Objects.nonNull(updateFish.description())) {
            oldFish.setDescription(updateFish.description());
        }
    }
}
