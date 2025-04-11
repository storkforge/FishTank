package org.example.fishtank;

import org.example.fishtank.model.entity.*;
import org.example.fishtank.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// This class is used to test the Spring Boot application
// It is annotated with @SpringBootTest to load the application context
// It is also annotated with @AutoConfigureMockMvc to enable MockMvc for testing
// The class is empty for now, but can be filled with test methods later

@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
//Configures mockmvc for testing the MVC controllers. MockMvc simulates http requests
@AutoConfigureMockMvc
//Enables containerized dependencies like postgreSQL to automaticly start and stop för each test
@Testcontainers
@Transactional
public class SpringBootIntegrationTest {

    //Testcontainer for PostgreSQL database
    @Container
    //Connects the container to the Spring Boot application
    @ServiceConnection

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    MockMvc mockMvc;

    @Autowired
    FishRepository fishRepository;
    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    SexRepository sexRepository;
    @Autowired
    AccessRepository accessRepository;
    @Autowired
    WaterTypeRepository waterTypeRepository;


    @BeforeEach
    void beforeEach() {

        System.out.println("Testing database connection: " + postgres.getJdbcUrl());

        var access = new Access();
        access.setName("Standard");
        accessRepository.save(access);

        var user = new AppUser();
        user.setName("username");
        user.setPasswordHash(new BCryptPasswordEncoder().encode("password")); // Encode the password
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
    @WithMockUser(username = "username")
    void getAllFishes() throws Exception {
        var result = mockMvc.perform(get("/my_fishes_rough")
                        .with(user("username")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.fishList[0].name").value("Fish"))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

}