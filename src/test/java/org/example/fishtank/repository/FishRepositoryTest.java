package org.example.fishtank.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.example.fishtank.model.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;



@DataJpaTest
@Testcontainers
class FishRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

    @Autowired FishRepository fishRepository;
    @Autowired UserRepository userRepository;
    @Autowired SexRepository sexRepository;
    @Autowired WaterTypeRepository waterTypeRepository;
    @Autowired AccessRepository accessRepository;

    @Transactional
    @BeforeEach
    void beforeEach() {
        try{
        var access = new Access();
        access.setName("Standard");
        accessRepository.save(access);


        var user = new AppUser();
        user.setName("username");
        user.setPasswordHash("password");
        user.setEmail("username@email.com");
        user.setAccess(access);
        userRepository.save(user);

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

    } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    void findAll() {
        var result = fishRepository.findAll();
        assertThat(result).hasSize(1);
    }


}