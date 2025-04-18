package org.example.fishtank.controller.resolver;

import org.example.fishtank.model.dto.fishDto.CreateFish;
import org.example.fishtank.model.dto.fishDto.ResponseFish;
import org.example.fishtank.model.dto.fishDto.UpdateFish;
import org.example.fishtank.model.dto.postDto.ResponsePost;
import org.example.fishtank.service.FishService;
import org.example.fishtank.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;

@GraphQlTest({FishResolver.class, PostResolver.class})
class FishResolverTest {

    @Autowired
    GraphQlTester graphQlTester;

    @MockBean
    FishService fishService;
    @MockBean
    PostService postService;

    List<ResponseFish> fishes = new ArrayList<>();

    @BeforeEach
    void setUp() {
        ResponseFish fish1 = new ResponseFish(
                1,
                "Test",
                "Aborre",
                "Simmar runt",
                "Fresh Water",
                "Male",
                "Test Testsson",
                "123456789_Aborre.png"
        );

        ResponseFish fish2 = new ResponseFish(
                2,
                "Test2",
                "Lax",
                "Chillar",
                "Salt Water",
                "Female",
                "Test testsson",
                "123456789_Lax.png"
        );

        fishes.add(fish1);
        fishes.add(fish2);
    }

    @Test
    void getAllFish() {
        when(fishService.getAllFish()).thenReturn(fishes);

        graphQlTester.document("""
                        query {
                            getAllFish {
                                id
                                name
                                description
                                waterType
                                sex
                                appUser
                                image
                            }
                        }
                        """)
                .execute()
                .path("getAllFish[0].id").entity(Integer.class).isEqualTo(1)
                .path("getAllFish[0].name").entity(String.class).isEqualTo("Test")
                .path("getAllFish[0].description").entity(String.class).isEqualTo("Simmar runt")
                .path("getAllFish[0].waterType").entity(String.class).isEqualTo("Fresh Water")
                .path("getAllFish[0].sex").entity(String.class).isEqualTo("Male")
                .path("getAllFish[0].appUser").entity(String.class).isEqualTo("Test Testsson")
                .path("getAllFish[0].image").entity(String.class).isEqualTo("123456789_Aborre.png");
    }

    @Test
    void getFishById() {
        ResponseFish fish = fishes.get(0);
        when(fishService.findById(1)).thenReturn(fish);

        System.out.println(fishService.findMyFishById(1));

        graphQlTester.document("""
                        query {
                            getFishById(id:1) {
                                id
                                name
                                description
                                waterType
                                sex
                                appUser
                                image
                            }
                        }
                        """)
                .execute()
                .path("getFishById.id").entity(Integer.class).isEqualTo(1)
                .path("getFishById.name").entity(String.class).isEqualTo("Test")
                .path("getFishById.description").entity(String.class).isEqualTo("Simmar runt")
                .path("getFishById.waterType").entity(String.class).isEqualTo("Fresh Water")
                .path("getFishById.sex").entity(String.class).isEqualTo("Male")
                .path("getFishById.appUser").entity(String.class).isEqualTo("Test Testsson")
                .path("getFishById.image").entity(String.class).isEqualTo("123456789_Aborre.png");
    }

    @Test
    void addFish() {
        CreateFish newMockFish = new CreateFish(
                "New fish",
                "Aborre",
                "Just created",
                "Fresh Water",
                "Male",
                "Test Testsson",
                "123456789_Aborre.png"
        );

        ResponseFish savedMockFish = new ResponseFish(
                1,
                "New fish",
                "Aborre",
                "Just created",
                "Fresh Water",
                "Male",
                "Test Testsson",
                "123456789_Aborre.png"
        );

        doNothing().when(fishService).save(newMockFish);
        when(fishService.findFishByName("New fish")).thenReturn(savedMockFish);

        graphQlTester.document("""
                        mutation {
                             addFish(
                             name: "New fish",
                             species: "Aborre",
                             description: "Just created",
                             waterType: "Fresh Water",
                             sex: "Male",
                             appUser: "Test Testsson",
                             image: "123456789_Aborre.png"
                             ) {
                                         id
                                         name
                                         description
                                         waterType
                                         sex
                                         appUser
                                         image
                             }
                         }
                        """)
                .execute()
                .path("addFish.id").entity(Integer.class).isEqualTo(1)
                .path("addFish.name").entity(String.class).isEqualTo("New fish")
                .path("addFish.description").entity(String.class).isEqualTo("Just created")
                .path("addFish.waterType").entity(String.class).isEqualTo("Fresh Water")
                .path("addFish.sex").entity(String.class).isEqualTo("Male")
                .path("addFish.appUser").entity(String.class).isEqualTo("Test Testsson")
                .path("addFish.image").entity(String.class).isEqualTo("123456789_Aborre.png");
    }

    @Test
    void updateFish() {
        UpdateFish updateFish = new UpdateFish(
                "Changed name",
                "Changed description"
        );

        ResponseFish updatedMockFish = new ResponseFish(
                1,
                "Changed name",
                "Aborre",
                "Changed description",
                "Fresh Water",
                "Male",
                "Test Testsson",
                "123456789_Aborre.png"
        );

        doNothing().when(fishService).update(1, updateFish);
        when(fishService.findFishByName("Changed name")).thenReturn(updatedMockFish);

        graphQlTester.document("""
                            mutation {
                              updateFish(id: 1, name: "Changed name", description: "Changed description") {
                                id
                                name
                                description
                              }
                            }
                        """)
                .execute()
                .path("updateFish.id").entity(Integer.class).isEqualTo(1)
                .path("updateFish.name").entity(String.class).isEqualTo("Changed name")
                .path("updateFish.description").entity(String.class).isEqualTo("Changed description");

        verify(fishService).update(eq(1), any(UpdateFish.class));
        verify(fishService).findFishByName("Changed name");
    }

    @Test
    void getFishFromPost() {
        ResponseFish mockFish = fishes.get(0);

        ResponsePost mockPost = new ResponsePost(
                1,
                "Testing a new post",
                "Gothenburg",
                1.0,
                1.0,
                1
        );

        ResponsePost mockPost2 = new ResponsePost(
                2,
                "Testing second post",
                "Gothenburg",
                1.0,
                1.0,
                1
        );

        List<ResponsePost> posts = new ArrayList<>();
        posts.add(mockPost);
        posts.add(mockPost2);

        when(fishService.findById(1)).thenReturn(mockFish);
        when(postService.getAllPost()).thenReturn(posts);

        graphQlTester.document("""
                                 query {
                                     getAllPost {
                                         id
                                         text
                                         fish {
                                             id
                                             name
                                         }
                                     }
                                 }
                        """)
                .execute()
                .path("getAllPost[0].fish.id").entity(Integer.class).isEqualTo(1)
                .path("getAllPost[0].fish.name").entity(String.class).isEqualTo("Test")
                .path("getAllPost[1].fish.id").entity(Integer.class).isEqualTo(1)
                .path("getAllPost[1].fish.name").entity(String.class).isEqualTo("Test");
    }
}