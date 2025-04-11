package org.example.fishtank;

import jakarta.persistence.EntityManager;
import org.example.fishtank.model.entity.*;
import org.example.fishtank.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// This class is used to test the Spring Boot application
// It is annotated with @SpringBootTest to load the application context
// It is also annotated with @AutoConfigureMockMvc to enable MockMvc for testing
// The class is empty for now, but can be filled with test methods later

@SpringBootTest(properties = {
        "spring.main.allow-bean-definition-overriding=true",
        "spring.sql.init.mode=never"})
//Configures mockmvc for testing the MVC controllers. MockMvc simulates http requests
@AutoConfigureMockMvc
//Enables containerized dependencies like postgreSQL to automaticly start and stop för each test
@Testcontainers
@ActiveProfiles("test")
@Import(TestCacheConfig.class)
public class SpringBootIntegrationTest {

    //Testcontainer for PostgreSQL database
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("testuser")
            .withPassword("testpass");

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

    @Autowired
    private Environment env;

    AppUser appUser;
    @Autowired
    EntityManager entityManager;

    @BeforeEach
    void beforeEach() {
        System.out.println("Testing database connection: " + postgres.getJdbcUrl());
        System.out.println("Current profile: " + Arrays.toString(env.getActiveProfiles()));

        fishRepository.deleteAll();
        appUserRepository.deleteAll();
        sexRepository.deleteAll();
        accessRepository.deleteAll();
        waterTypeRepository.deleteAll();

        entityManager.clear();  // Clears the Hibernate cache/session

        var access = new Access();
        access.setName("Standard");
        accessRepository.save(access);

        appUser = new AppUser();
        appUser.setName("username");
        appUser.setPasswordHash(new BCryptPasswordEncoder().encode("password")); // Encode the password
        appUser.setEmail("username@email.com");
        appUser.setAccess(access);
        appUser.setAuthenticationCode("hej");
        appUserRepository.save(appUser);

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
        fish.setAppUser(appUser);
        fish.setWaterType(waterType);
        fishRepository.save(fish);
    }

    @Test
    void getAllFishes() throws Exception {
        mockMvc.perform(get("/my_fishes_rough")
                .with(user(appUser.getName()).password("password").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.fishList.length()").value(1))
                .andExpect(jsonPath("$.fishList[0].name").value("Fish"))
                .andExpect(jsonPath("$.fishList[0].species").value("Eel"))
                .andReturn();

    }

}