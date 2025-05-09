package org.example.fishtank.repository;

import jakarta.persistence.EntityManager;
import org.example.fishtank.model.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
class FishRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgisContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgis/postgis:15-3.3")
                    .asCompatibleSubstituteFor("postgres") // critical for compatibility
    );

    @Autowired
    FishRepository fishRepository;
    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    SexRepository sexRepository;
    @Autowired
    WaterTypeRepository waterTypeRepository;
    @Autowired
    AccessRepository accessRepository;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void beforeEach() {
        var access = new Access();
        access.setName("Standard");
        accessRepository.save(access);

        var user = new AppUser();
        user.setName("username");
        user.setPasswordHash("password");
        user.setEmail("username@email.com");
        user.setAccess(access);
        user.setAuthenticationCode("hej");
        appUserRepository.save(user);

        var sex = new Sex();
        sex.setName("Male");
        sexRepository.save(sex);

        var waterType = new WaterType();
        waterType.setName("Salt water");
        waterTypeRepository.save(waterType);

        var fish = new Fish();
        fish.setName("Fish");
        fish.setSpecies("Eel");
        fish.setDescription("a fish");
        fish.setSex(sex);
        fish.setAppUser(user);
        fish.setWaterType(waterType);
        fishRepository.save(fish);
    }

    @Test
    void findAll() {
        var result = fishRepository.findAll();
        assertThat(result).hasSize(1);
    }

    @Test
    void updateFish() {
        String newName = "New Fish Name";
        String newDescription = "New Fish Description";
        Fish fish = fishRepository.findAll().getFirst();
        int fishId = fish.getId();

        fishRepository.update(newName, newDescription, fishId);
        entityManager.flush();
        entityManager.clear();

        Fish updatedFish = fishRepository.findById(fishId).orElse(null);
        assertThat(updatedFish).isNotNull();
        assertThat(updatedFish.getName()).isEqualTo(newName);
        assertThat(updatedFish.getDescription()).isEqualTo(newDescription);
        //
    }
}
