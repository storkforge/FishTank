package org.example.fishtank.model.mapper;

import org.example.fishtank.model.dto.fishDto.CreateFish;
import org.example.fishtank.model.dto.fishDto.ResponseFish;
import org.example.fishtank.model.dto.fishDto.UpdateFish;
import org.example.fishtank.model.dto.postDto.ResponsePost;
import org.example.fishtank.model.entity.*;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.geolatte.geom.builder.DSL;
import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FishMapperTest {

    WaterType waterTypeTest = new WaterType();
    Sex sexTest = new Sex();
    AppUser appUserTest = new AppUser();
    String imageTest = "testImage.png";

    @BeforeEach()
    void setUp() {
        waterTypeTest.setId(1);
        waterTypeTest.setName("Salt Water");

        appUserTest.setId(1);
        appUserTest.setName("testAppUser");

        sexTest.setId(1);
        sexTest.setName("testSex");
    }

    @Test
    @DisplayName("Map Fish to ResponseFish should return same ")
    void mapFishToResponseFishShouldReturnSame() {
        Fish fish = new Fish();
        fish.setId(1);
        fish.setName("fishTestName");
        fish.setSpecies("fishTestSpecies");
        fish.setDescription("fishTestDescription");
        fish.setWaterType(waterTypeTest);
        fish.setAppUser(appUserTest);
        fish.setSex(sexTest);
        fish.setImage(imageTest);

        ResponseFish responseFish = FishMapper.map(fish);

        assertNotNull(responseFish);
        assertEquals(1, responseFish.id());
        assertEquals("fishTestName", responseFish.name());
        assertEquals("fishTestSpecies", responseFish.species());
        assertEquals("fishTestDescription", responseFish.description());
        assertEquals(waterTypeTest.getName(), responseFish.waterType());
        assertEquals(appUserTest.getName(), responseFish.appUser());
        assertEquals(sexTest.getName(), responseFish.sex());
        assertEquals(imageTest, responseFish.image());
    }

    @Test
    @DisplayName("Map Null Fish to ResponseFish")
    void mapNullFishToResponseFish(){
        ResponseFish responseFish = FishMapper.map(null);
        assertNull(responseFish);
    }

    @Test
    @DisplayName("Map CreateFish to fish should be same")
    void mapCreateFishToFishShouldBeSame() {
        CreateFish createFish = new CreateFish(
                "fishTestName",
                "fishTestSpecies",
                "fishTestDescription",
                "Salt Water",
                "testSex",
                "testAppUser",
                "testImage.png"
        );

        Fish fish = FishMapper.map(createFish, waterTypeTest ,sexTest, appUserTest);
        assertNotNull(fish);
        assertEquals("fishTestName", fish.getName());
        assertEquals("fishTestSpecies", fish.getSpecies());
        assertEquals("fishTestDescription", fish.getDescription());
        assertEquals(waterTypeTest.getName(), fish.getWaterType().getName());
        assertEquals(sexTest.getName(), fish.getSex().getName());
        assertEquals(appUserTest.getName(), fish.getAppUser().getName());
        assertEquals("testImage.png", fish.getImage());
    }

    @Test
    @DisplayName("Map Null CreateFish to Fish")
    void mapNullCreateFishToFish(){
        Fish fish = FishMapper.map(null,waterTypeTest,sexTest,appUserTest);
        assertNull(fish);

    }

    @Test
    @DisplayName("Map UpdateFish to Fish should update fish")
    void mapUpdateFishToFishShouldUpdateFish(){
        Fish oldFish = new Fish();
        oldFish.setId(1);
        oldFish.setName("fishTestName");
        oldFish.setDescription("fishTestDescription");

        UpdateFish updateFish = new UpdateFish("Updated name", "Updated description");
        FishMapper.map(updateFish,oldFish);

        assertEquals("Updated name", oldFish.getName());
        assertEquals("Updated description", oldFish.getDescription());

    }
    @Test
    @DisplayName("Map UpdateFish to fish should update only name")
    void mapUpdateFishToFishShouldUpdateOnlyName(){
        Fish oldFish = new Fish();
        oldFish.setId(1);
        oldFish.setName("fishTestName");
        oldFish.setDescription("fishTestDescription");

        UpdateFish updateFish = new UpdateFish("Updated name", null);
        FishMapper.map(updateFish,oldFish);
        assertEquals("Updated name", oldFish.getName());
        assertEquals("fishTestDescription", oldFish.getDescription());
    }
    @Test
    @DisplayName("Map UpdateFish to fish should update only description")
    void mapUpdateFishToFishShouldUpdateOnlyDescription(){
        Fish oldFish = new Fish();
        oldFish.setId(1);
        oldFish.setName("fishTestName");
        oldFish.setDescription("fishTestDescription");

        UpdateFish updateFish = new UpdateFish(null, "Updated description");
        FishMapper.map(updateFish,oldFish);
        assertEquals("fishTestName", oldFish.getName());
        assertEquals("Updated description", oldFish.getDescription());
    }
}
