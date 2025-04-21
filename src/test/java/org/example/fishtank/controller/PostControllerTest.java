package org.example.fishtank.controller;


import org.example.fishtank.model.dto.fishDto.ResponseFish;
import org.example.fishtank.model.dto.postDto.ResponsePost;
import org.example.fishtank.service.FishService;
import org.example.fishtank.service.GeoService;
import org.example.fishtank.service.PostService;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.geolatte.geom.builder.DSL;
import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private GeoService geoService;

    @MockitoBean
    private FishService fishService;

    @MockitoBean
    private PostService postService;

    @Test
    @WithMockUser
    void testGetPostRoughById() throws Exception {
        int postId = 1;

        ResponsePost mockPost = new ResponsePost(
                1,
                "test text",
                "Göteborg",
                20.0,
                20.0,
                1
        );

        when(postService.findById(postId)).thenReturn(mockPost);

        mockMvc.perform(get("/forum_rough/{id}", postId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(postId))
                .andExpect(jsonPath("$.text").value("test text"))
                .andExpect(jsonPath("$.cityName").value("Göteborg"))
                .andExpect(jsonPath("$.longitude").value(20.0))
                .andExpect(jsonPath("$.latitude").value(20.0))
                .andExpect(jsonPath("$.fishId").value(1));
    }

    @Test
    @WithMockUser
    void forumRough() throws Exception {
        List<ResponsePost> postList = List.of(new ResponsePost(
                1,
                "test text1",
                "Göteborg",
                20.0,
                20.0,
                1
        ), new ResponsePost(2,
                "test text2",
                "Göteborg",
                20.0,
                20.0,
                1));

        when(postService.getAllPost()).thenReturn(postList);


        mockMvc.perform(get("/forum_rough"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.postList[0].id").value(1))
                .andExpect(jsonPath("$.postList[0].text").value("test text1"))
                .andExpect(jsonPath("$.postList[0].cityName").value("Göteborg"))
                .andExpect(jsonPath("$.postList[0].longitude").value(20.0))
                .andExpect(jsonPath("$.postList[0].latitude").value(20.0))
                .andExpect(jsonPath("$.postList[0].fishId").value(1))
                .andExpect(jsonPath("$.postList[1].id").value(2))
                .andExpect(jsonPath("$.postList[1].text").value("test text2"))
                .andExpect(jsonPath("$.postList[1].cityName").value("Göteborg"))
                .andExpect(jsonPath("$.postList[1].longitude").value(20.0))
                .andExpect(jsonPath("$.postList[1].latitude").value(20.0))
                .andExpect(jsonPath("$.postList[1].fishId").value(1));


    }

    @Test
    @WithMockUser
    void getAddPostShouldReturnViewWithFishes() throws Exception {
        List<ResponseFish> fishList = List.of(new ResponseFish(1, "fishTest", "speciesTest", "descriptionTest", "waterTest", "sexTest", "userTest", "imageTest.png"));
        when(fishService.getMyFish()).thenReturn(fishList);

        mockMvc.perform(get("/add_post"))
                .andExpect(status().isOk())
                .andExpect(view().name("add_post"))
                .andExpect(model().attributeExists("fishList"))
                .andExpect(model().attribute("fishList", fishList));
    }

    @Test
    @WithMockUser
    void postAddPostShouldCreateNewPost() throws Exception {

        List<ResponseFish> fishList = List.of(new ResponseFish(1, "fishTest", "speciesTest", "descriptionTest", "waterTest", "sexTest", "userTest", "imageTest.png"));
        when(fishService.getMyFish()).thenReturn(fishList);

        mockMvc.perform(post("/add_post")
                        .param("fishId", "1")
                        .param("text", "testText")
                        .param("cityName", "testCity")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forum"));
    }

    @Test
    @WithMockUser
    void getUpdatePostShouldReturnViewWithPosts() throws Exception {
        int postId = 1;

        ResponsePost responsePost = new ResponsePost(postId, "testText", "Göteborg", 25.0, 25.0, 1);
        when(postService.findByMyId(postId)).thenReturn(responsePost);
        mockMvc.perform(get("/update_post/{id}", responsePost.id()))
                .andExpect(status().isOk())
                .andExpect(view().name("update_post"))
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", responsePost));
    }

    @Test
    @WithMockUser
    void postUpdatePostShouldUpdatePostAndRedirect() throws Exception {
        int postId = 1;

        mockMvc.perform(post("/update_post/{id}", postId)
                        .param("text", "testText")
                        .param("cityName", "testCity")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my_posts"));

        verify(postService).update(eq(postId), argThat(updatePost ->
                updatePost.text().equals("testText") && updatePost.cityName().equals("testCity")
        ));
    }

    @Test
    @WithMockUser
    void postDeletePostShouldDeleteAndRedirect() throws Exception {
        int postId = 1;
        mockMvc.perform(post("/delete_post/{id}", postId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my_posts"));
        verify(postService).delete(postId);
    }

    @Test
    @WithMockUser
    void getMyPostsByIdShouldReturnMyPost() throws Exception {
        ResponseFish responseFish1 = new ResponseFish(1,
                "Ogge",
                "Guppy",
                "Fin fisk med fint hjärta",
                "Freshwater",
                "Male",
                "Oggeboi",
                "betta.jpg");

        ResponsePost responsePost1 = new ResponsePost(1, "testText", "testCity", 25.0, 25.0, 1);

        when(postService.findByMyId(1)).thenReturn(responsePost1);
        when(fishService.findById(1)).thenReturn(responseFish1);

        mockMvc.perform(get("/my_posts/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attributeExists("fish"))
                .andExpect(model().attribute("post", responsePost1))
                .andExpect(model().attribute("fish", responseFish1));
    }

    @Test
    @WithMockUser
    void getMyPostsShouldReturnMyPosts() throws Exception {

        List<ResponsePost> postList = List.of(new ResponsePost(
                1,
                "test text1",
                "Göteborg",
                20.0,
                20.0,
                1
        ));
        List<ResponseFish> fishList = List.of(new ResponseFish(1,
                "Ogge",
                "Guppy",
                "Fin fisk med fint hjärta",
                "Freshwater",
                "Male",
                "Oggeboi",
                "betta.jpg"));

        when(postService.getAllMyPosts()).thenReturn(postList);
        when(fishService.getFishByPost(postList)).thenReturn(fishList);

        mockMvc.perform(get("/my_posts"))
                .andExpect(status().isOk())
                .andExpect(view().name("my_posts"))
                .andExpect(model().attributeExists("postList"))
                .andExpect(model().attributeExists("fishList"))
                .andExpect(model().attribute("postList", postList))
                .andExpect(model().attribute("fishList", fishList));

    }

    @Test
    @WithMockUser
    void getForumReturnsForum() throws Exception {
        String location = "Göteborg";
        double radius = 25.0;

        List<ResponseFish> fishList = List.of(new ResponseFish(1,
                "Ogge",
                "Guppy",
                "Fin fisk med fint hjärta",
                "Freshwater",
                "Male",
                "Oggeboi",
                "betta.jpg"));

        List<ResponsePost> allPosts = List.of(
                new ResponsePost(1, "testText1", "Göteborg", 57.7089, 11.9746, 1),
                new ResponsePost(2, "testText2", "Stockholm", 59.3293, 18.0686, 1)
        );
        Point<G2D> coordinate = DSL.point(
                CoordinateReferenceSystems.WGS84, DSL.g(57.7089, 11.9746));

        when(postService.getAllPost()).thenReturn(allPosts);
        when(geoService.geocodeCity(location)).thenReturn(coordinate);
        when(fishService.findById(1)).thenReturn(fishList.getFirst());


        mockMvc.perform(get("/forum")
                        .param("location", location)
                        .param("radius", String.valueOf(radius)))
                .andExpect(status().isOk())
                .andExpect(view().name("forum"))
                .andExpect(model().attributeExists("postList"))
                .andExpect(model().attributeExists("fishList"))
                .andExpect(model().attribute("postList", List.of(allPosts.getFirst())))
                .andExpect(model().attribute("fishList", fishList))
                .andExpect(model().attribute("location", location))
                .andExpect(model().attribute("radius", radius));
    }

    @Test
    @WithMockUser
    void getForumWithLocationAndInvalidGeocodeReturnsAllPosts() throws Exception {
        String location = "InvalidCity";
        double radius = 25.0;

        List<ResponsePost> allPosts = List.of(
                new ResponsePost(1, "testText1", "Göteborg", 57.7089, 11.9746, 1),
                new ResponsePost(2, "testText2", "Stockholm", 59.3293, 18.0686, 2)
        );
        List<ResponseFish> fishList = List.of(
                new ResponseFish(1, "Ogge", "Guppy", "Fin fisk med fint hjärta", "Freshwater", "Male", "Oggeboi", "betta.jpg"),
                new ResponseFish(2, "Trout", "Trout", "Freshwater fish", "Freshwater", "Female", "Trouty", "trout.jpg")
        );

        when(postService.getAllPost()).thenReturn(allPosts);
        when(geoService.geocodeCity(location)).thenReturn(null); // Invalid geocode
        when(fishService.findById(1)).thenReturn(fishList.get(0));
        when(fishService.findById(2)).thenReturn(fishList.get(1));

        mockMvc.perform(get("/forum")
                        .param("location", location)
                        .param("radius", String.valueOf(radius)))
                .andExpect(status().isOk())
                .andExpect(view().name("forum"))
                .andExpect(model().attributeExists("postList"))
                .andExpect(model().attributeExists("fishList"))
                .andExpect(model().attribute("postList", allPosts))
                .andExpect(model().attribute("fishList", fishList))
                .andExpect(model().attribute("location", location))
                .andExpect(model().attribute("radius", radius));
    }

    @Test
    @WithMockUser
    void getForumWithoutLocationReturnsAllPosts() throws Exception {
        double radius = 25.0;

        List<ResponsePost> allPosts = List.of(
                new ResponsePost(1, "testText1", "Göteborg", 57.7089, 11.9746, 1),
                new ResponsePost(2, "testText2", "Stockholm", 59.3293, 18.0686, 2)
        );
        List<ResponseFish> fishList = List.of(
                new ResponseFish(1, "Ogge", "Guppy", "Fin fisk med fint hjärta", "Freshwater", "Male", "Oggeboi", "betta.jpg"),
                new ResponseFish(2, "Trout", "Trout", "Freshwater fish", "Freshwater", "Female", "Trouty", "trout.jpg")
        );

        when(postService.getAllPost()).thenReturn(allPosts);
        when(fishService.findById(1)).thenReturn(fishList.get(0));
        when(fishService.findById(2)).thenReturn(fishList.get(1));

        mockMvc.perform(get("/forum")
                        .param("radius", String.valueOf(radius)))
                .andExpect(status().isOk())
                .andExpect(view().name("forum"))
                .andExpect(model().attributeExists("postList"))
                .andExpect(model().attributeExists("fishList"))
                .andExpect(model().attribute("postList", allPosts))
                .andExpect(model().attribute("fishList", fishList))
                .andExpect(model().attribute("location", (Object) null)) // Ensure null is explicitly cast
                .andExpect(model().attribute("radius", radius));
    }

    @Test
    @WithMockUser
    void getForumWithNullCoordinatesReturnsFilteredPosts() throws Exception {
        String location = "Göteborg";
        double radius = 25.0;

        List<ResponsePost> allPosts = List.of(
                new ResponsePost(1, "testText1", "Göteborg", null, null, 1),
                new ResponsePost(2, "testText2", "Stockholm", 59.3293, 18.0686, 2),
                new ResponsePost(3, "testText3", "Malmö", 55.6050, 13.0038, 3)
        );
        Point<G2D> coordinate = DSL.point(
                CoordinateReferenceSystems.WGS84, DSL.g(57.7089, 11.9746));

        when(postService.getAllPost()).thenReturn(allPosts);
        when(geoService.geocodeCity(location)).thenReturn(coordinate);
        when(fishService.findById(1)).thenReturn(new ResponseFish(1, "Ogge", "Guppy", "Fin fisk med fint hjärta", "Freshwater", "Male", "Oggeboi", "betta.jpg"));
        when(fishService.findById(2)).thenReturn(new ResponseFish(2, "Trout", "Trout", "Freshwater fish", "Freshwater", "Female", "Trouty", "trout.jpg"));
        when(fishService.findById(3)).thenReturn(new ResponseFish(3, "Salmon", "Salmon", "Freshwater fish", "Freshwater", "Male", "Salmony", "salmon.jpg"));

        mockMvc.perform(get("/forum")
                        .param("location", location)
                        .param("radius", String.valueOf(radius)))
                .andExpect(status().isOk())
                .andExpect(view().name("forum"))
                .andExpect(model().attributeExists("postList"))
                .andExpect(model().attributeExists("fishList"))
                .andExpect(model().attribute("postList", List.of())) // No posts within the radius
                .andExpect(model().attribute("fishList", List.of()))
                .andExpect(model().attribute("location", location))
                .andExpect(model().attribute("radius", radius));
    }




    @Test
    @WithMockUser
    void getForumMapShouldReturnForumMap() throws Exception {
        List<ResponsePost> allPosts = List.of(
                new ResponsePost(1, "testText1", "Göteborg", 57.7089, 11.9746, 1),
                new ResponsePost(2, "testText2", "Stockholm", 59.3293, 18.0686, 1)
        );
        when(postService.getAllPost()).thenReturn(allPosts);
        mockMvc.perform(get("/forum/map"))
                .andExpect(status().isOk())
                .andExpect(view().name("forum_map"))
                .andExpect(model().attributeExists("postList"))
                .andExpect(model().attribute("postList", allPosts));

    }

}
