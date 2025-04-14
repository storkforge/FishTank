package org.example.fishtank;

import org.example.fishtank.model.entity.*;
import org.example.fishtank.repository.*;
import org.example.fishtank.service.CurrentUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Arrays;
import java.util.UUID;

import static org.mockito.Mockito.mockStatic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// This class is used to test the Spring Boot application
// It is annotated with @SpringBootTest to load the application context
// It is also annotated with @AutoConfigureMockMvc to enable MockMvc for testing

@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
//Configures mockmvc for testing the MVC controllers. MockMvc simulates http requests
@AutoConfigureMockMvc
//Enables containerized dependencies like postgreSQL to automaticly start and stop för each test
@Testcontainers
@Import(TestCacheConfig.class)
@ExtendWith(MockitoExtension.class)
public class SpringBootIntegrationTest {

    //Testcontainer for PostgreSQL database
    static PostgreSQLContainer<?> postgis = new PostgreSQLContainer<>(
            DockerImageName.parse("postgis/postgis:15-3.3")
                    .asCompatibleSubstituteFor("postgres"))
            .withInitScript("init-postgis.sql");


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
    PostRepository postRepository;

    @Autowired
    private Environment env;

    MockedStatic<CurrentUser> mockedStatic;

    Fish testFish;
    Post testPost;



    @BeforeEach
    void beforeEach() {
        System.out.println("Testing database connection: " + postgis.getJdbcUrl());
        System.out.println("Current profile: " + Arrays.toString(env.getActiveProfiles()));

        postRepository.deleteAll();
        fishRepository.deleteAll();
        appUserRepository.deleteAll();
        sexRepository.deleteAll();
        waterTypeRepository.deleteAll();
        accessRepository.deleteAll();


        var access = new Access();
        access.setName("Standard");
        accessRepository.save(access);

        var user = new AppUser();
        user.setName("username");
        user.setPasswordHash(new BCryptPasswordEncoder().encode("password")); // Encode the password
        user.setEmail("username@email.com");
        user.setAccess(access);
        user.setAuthenticationCode(UUID.randomUUID().toString());
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
        testFish = fishRepository.save(fish);

        var post = new Post();
        post.setText("Test post");
        post.setFishid(fish);
        testPost = postRepository.save(post);


        mockedStatic = mockStatic(CurrentUser.class);
        mockedStatic.when(CurrentUser::getId).thenReturn(user.getId());

    }

    @AfterEach
    void tearDown() {
        mockedStatic.close();
    }

    @Test
    void unauthorizedUserShouldBeRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/my_fishes_rough"))
                .andExpect(status().isFound());
    }

    @Test
    void myFishesRoughWithIdShouldReturnTheRightFish() throws Exception {
        mockMvc.perform(get("/my_fishes_rough/" + testFish.getId())
                        .with(user("username")))
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value("Fish"))
                .andExpect(jsonPath("$.species").value("Eel"))
                .andExpect(jsonPath("$.description").value("a fish"))
                .andExpect(jsonPath("$.waterType").value("Salt water"))
                .andExpect(jsonPath("$.sex").value("Male"))
                .andExpect(jsonPath("$.appUser").value("username"))
                .andReturn();
    }

    @Test
    void myFishesRoughShouldReturnTheRightFish() throws Exception {
        mockMvc.perform(get("/my_fishes_rough")
                        .with(user("username")))
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.fishList.length()").value(1))
                .andExpect(jsonPath("$.fishList[0].name").value("Fish"))
                .andExpect(jsonPath("$.fishList[0].species").value("Eel"))
                .andExpect(jsonPath("$.fishList[0].description").value("a fish"))
                .andExpect(jsonPath("$.fishList[0].waterType").value("Salt water"))
                .andExpect(jsonPath("$.fishList[0].sex").value("Male"))
                .andExpect(jsonPath("$.fishList[0].appUser").value("username"))

                .andReturn();
    }

    @Test
    void forumRoughShouldReturnReturnRightPost() throws Exception {
        mockMvc.perform(get("/forum_rough")
                        .with(user("username")))
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.postList.length()").value(1))
                .andExpect(jsonPath("$.postList[0].text").value("Test post"))
                .andReturn();
    }

    @Test
    void forumRoughWithIdShouldReturnReturnRightPost() throws Exception {
        mockMvc.perform(get("/forum_rough/1")
                        .with(user("username")))
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.text").value("Test post"))
                .andReturn();
    }


}
