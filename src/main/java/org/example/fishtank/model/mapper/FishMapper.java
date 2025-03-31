package org.example.fishtank.model.mapper;

import org.example.fishtank.model.dto.fishDto.CreateFish;
import org.example.fishtank.model.dto.fishDto.ResponseFish;
import org.example.fishtank.model.dto.fishDto.UpdateFish;
import org.example.fishtank.model.entity.AppUser;
import org.example.fishtank.model.entity.Fish;
import org.example.fishtank.model.entity.Sex;
import org.example.fishtank.model.entity.WaterType;

public class FishMapper {

    public static ResponseFish map(Fish fish) {
        if (null == fish)
            return null;
        return new ResponseFish(
                fish.getId(),
                fish.getName(), fish.getSpecies(),
                fish.getDescription(),
                fish.getWaterType().getName(),
                fish.getSex().getName(),
                fish.getAppUser().getName());
    }

    public static Fish map(CreateFish createFish) {
        if (null == createFish)
            return null;
        Fish fish = new Fish();
        WaterType waterType = new WaterType();
        waterType.setName(createFish.name());
        Sex sex = new Sex();
        sex.setName(createFish.sex());
        AppUser appUser = new AppUser();
        appUser.setName(createFish.name());

        fish.setName(createFish.name());
        fish.setSpecies(createFish.species());
        fish.setDescription(createFish.description());
        fish.setWaterType(waterType);
        fish.setSex(sex);
        fish.setAppUser(appUser);
        return fish;
    }

    public static void map(UpdateFish updateFish, Fish oldFish) {
        if (updateFish.name() != null) {
            oldFish.setName(updateFish.name());
        }
        if (updateFish.description() != null) {
            oldFish.setDescription(updateFish.description());
        }
    }
}
