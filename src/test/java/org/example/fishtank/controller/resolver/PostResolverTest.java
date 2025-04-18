package org.example.fishtank.controller.resolver;

import org.example.fishtank.model.dto.fishDto.ResponseFish;
import org.example.fishtank.model.dto.postDto.CreatePost;
import org.example.fishtank.model.dto.postDto.ResponsePost;
import org.example.fishtank.model.dto.postDto.UpdatePost;
import org.example.fishtank.model.entity.Fish;
import org.example.fishtank.model.entity.Post;
import org.example.fishtank.service.FishService;
import org.example.fishtank.service.PostService;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@GraphQlTest({PostResolver.class, FishResolver.class})
class PostResolverTest {

    @Autowired
    GraphQlTester graphQlTester;

    @MockBean
    PostService postService;
    @MockBean
    FishService fishService;

    List<ResponsePost> posts = new ArrayList<>();

    @BeforeEach
    void setUp() {
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
        posts.add(mockPost);
        posts.add(mockPost2);
    }


    @Test
    void getAllPost() {
        when(postService.getAllPost()).thenReturn(posts);

        graphQlTester.document("""
                                 query {
                                     getAllPost {
                                         id
                                         text
                                     }
                                 }
                        """)
                .execute()
                .path("getAllPost[0].id").entity(Integer.class).isEqualTo(1)
                .path("getAllPost[0].text").entity(String.class).isEqualTo("Testing a new post")
                .path("getAllPost[1].id").entity(Integer.class).isEqualTo(2)
                .path("getAllPost[1].text").entity(String.class).isEqualTo("Testing second post");
    }

    @Test
    void getAllPostByFishId() {
        ResponseFish mockFish = new ResponseFish(
                1,
                "Test",
                "Aborre",
                "Simmar runt",
                "Fresh Water",
                "Male",
                "Test Testsson",
                "123456789_Aborre.png"
        );

        when(postService.findByFishId(1)).thenReturn(posts);
        when(fishService.findById(1)).thenReturn(mockFish);

        graphQlTester.document("""
                        query {
                            getAllPostByFishId(id: 1) {
                                id
                                text
                                fish {
                                    id
                                }
                            }
                        }
                        """)
                .execute()
                .path("getAllPostByFishId[0].id").entity(Integer.class).isEqualTo(1)
                .path("getAllPostByFishId[0].text").entity(String.class).isEqualTo("Testing a new post")
                .path("getAllPostByFishId[0].fish.id").entity(Integer.class).isEqualTo(1)
                .path("getAllPostByFishId[1].id").entity(Integer.class).isEqualTo(2)
                .path("getAllPostByFishId[1].text").entity(String.class).isEqualTo("Testing second post")
                .path("getAllPostByFishId[0].fish.id").entity(Integer.class).isEqualTo(1);
    }

    @Test
    void addPost() {
        G2D mockG2D = mock(G2D.class);
        when(mockG2D.getLon()).thenReturn(11.97);
        when(mockG2D.getLat()).thenReturn(57.71);

        Point<G2D> mockPoint = mock(Point.class);
        when(mockPoint.getPosition()).thenReturn(mockG2D);

        Post savedMockPost = new Post();
        savedMockPost.setId(1);
        savedMockPost.setText("Adding new post");
        savedMockPost.setCoordinate(mockPoint);

        Fish mockFish = new Fish();
        mockFish.setId(1);
        savedMockPost.setFishid(mockFish);

        when(postService.saveAndReturn(any(CreatePost.class))).thenReturn(savedMockPost);

        graphQlTester.document("""
                            mutation {
                                addPost (
                                      text: "Adding new post",
                                      fishId: 1 ) {
                                    id
                                    text
                                    }
                            }
                        """)
                .execute()
                .path("addPost.id").entity(Integer.class).isEqualTo(1)
                .path("addPost.text").entity(String.class).isEqualTo("Adding new post");
    }

    @Test
    void updatePost() {
        ResponsePost updatedMockPost = new ResponsePost(
                1,
                "Updated post",
                "Gothenburg",
                1.0,
                1.0,
                1
        );
        doNothing().when(postService).update(eq(1), any(UpdatePost.class));
        when(postService.findById(1)).thenReturn(updatedMockPost);

        graphQlTester.document("""
                        mutation {
                            updatePost(id: 1, text: "Updated post") {
                                id
                                text
                            }
                        }
                        """)
                .execute()
                .path("updatePost.id").entity(Integer.class).isEqualTo(1)
                .path("updatePost.text").entity(String.class).isEqualTo("Updated post");
    }

    @Test
    void getPostsForFish() {
        List<ResponseFish> fishes = new ArrayList<>();

        ResponseFish mockFish = new ResponseFish(
                1,
                "Test",
                "Aborre",
                "Simmar runt",
                "Fresh Water",
                "Male",
                "Test Testsson",
                "123456789_Aborre.png"
        );

        fishes.add(mockFish);
        when(fishService.getAllFish()).thenReturn(fishes);
        when(postService.findByFishId(1)).thenReturn(posts);

        graphQlTester.document("""
                        query {
                            getAllFish {
                                id
                                name
                                posts {
                                    id
                                    text
                                }
                            }
                        }
                        """)
                .execute()
                .path("getAllFish[0].posts[0].id").entity(Integer.class).isEqualTo(1)
                .path("getAllFish[0].posts[0].text").entity(String.class).isEqualTo("Testing a new post")
                .path("getAllFish[0].posts[1].id").entity(Integer.class).isEqualTo(2)
                .path("getAllFish[0].posts[1].text").entity(String.class).isEqualTo("Testing second post");
    }
}