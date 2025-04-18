package org.example.fishtank.service;

import org.example.fishtank.model.dto.postDto.CreatePost;
import org.example.fishtank.model.dto.postDto.ResponsePost;
import org.example.fishtank.model.dto.postDto.UpdatePost;
import org.example.fishtank.model.entity.*;
import org.example.fishtank.model.mapper.PostMapper;
import org.example.fishtank.repository.*;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    PostService postService;

    @Mock
    private GeoService geoService;

    @Mock
    FishRepository fishRepository;

    @Mock
    PostRepository postRepository;
    MockedStatic<CurrentUser> mockedStatic;
    Post postTest = new Post();
    Fish fishTest = new Fish();
    WaterType waterTypeTest = new WaterType();
    Integer userID = 1;
    @Mock
    private PostMapper postMapper;

    @BeforeEach
    void setUp() {
        waterTypeTest.setId(1);
        waterTypeTest.setName("Salt Water");

        fishTest.setId(1);
        fishTest.setName("testFish");
        fishTest.setDescription("testFishDescription");
        fishTest.setSpecies("testFishSpecies");
        fishTest.setWaterType(waterTypeTest);

        postTest.setId(1);
        postTest.setText("test");
        postTest.setFishid(fishTest);

        mockedStatic = mockStatic(CurrentUser.class);
        mockedStatic.when(CurrentUser::getId).thenReturn(userID);

    }

    @AfterEach
    void tearDown() {
        mockedStatic.close();
    }

    @Test
    @DisplayName("FindById returns correct responsePost with correct values")
    void findByIdReturnsCorrectResponsePostWithCorrectValues() {
        ResponsePost responsePost = new ResponsePost(1, "test", null, null, null, 1);

        when(postRepository.findById(1)).thenReturn(Optional.of(postTest));
        assertEquals(responsePost, postService.findById(1));
    }

    @Test
    @DisplayName("NotFound is thrown when findById can not find the post")
    void notFoundIsThrownWhenFindByIdCanNotFindThePost() {
        when(postRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> postService.findById(1));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Post not found", exception.getReason());
    }

    @Test
    @DisplayName("findByMyId returns correct")
    void findByMyIdReturnsCorrect() {
        Integer postId = 1;
        Fish fish1 = new Fish();
        fish1.setId(1);
        List<Fish> fishList = List.of(fish1);
        Post post = new Post();
        post.setId(postId);
        post.setText("test");
        post.setFishid(fish1);

        when(fishRepository.findByAppUserId(userID)).thenReturn(fishList);
        when(postRepository.findByFishId(fish1.getId())).thenReturn(List.of(post));

        ResponsePost result = postService.findByMyId(postId);

        assertNotNull(result);
        assertEquals(postId, result.id());
        assertEquals("test", result.text());
        verify(fishRepository, times(1)).findByAppUserId(userID);
        verify(postRepository, times(1)).findByFishId(fish1.getId());
    }

    @Test
    @DisplayName("Get all Posts returns list of response posts")
    void getAllPostsReturnsListOfResponsePosts() {
        List<Post> posts = new ArrayList<>();
        Post postTest2 = new Post();
        postTest2.setId(2);
        postTest2.setText("test2");
        postTest2.setFishid(fishTest);

        List<ResponsePost> responsePosts = new ArrayList<>();
        ResponsePost responsePost = new ResponsePost(1, "test", null, null, null, 1);
        ResponsePost responsePost2 = new ResponsePost(2, "test2", null, null, null, 1);

        responsePosts.add(responsePost);
        responsePosts.add(responsePost2);

        posts.add(postTest);
        posts.add(postTest2);

        when(postRepository.findAll()).thenReturn(posts);
        assertEquals(responsePosts, postService.getAllPost());
    }

    @Test
    @DisplayName("getAllMyPosts returns my posts")
    void getAllMyPostsReturnsMyPosts() {

        Integer userId = 1;
        Fish fish1 = new Fish();
        fish1.setId(1);
        Fish fish2 = new Fish();
        fish2.setId(2);
        List<Fish> fishList = List.of(fish1, fish2);

        Post post1 = new Post();
        post1.setId(1);
        post1.setText("test1");
        post1.setFishid(fish1);
        Post post2 = new Post();
        post2.setId(2);
        post2.setText("test2");
        post2.setFishid(fish2);


        when(fishRepository.findByAppUserId(userId)).thenReturn(fishList);
        when(postRepository.findByFishId(fish1.getId())).thenReturn(List.of(post1));
        when(postRepository.findByFishId(fish2.getId())).thenReturn(List.of(post2));

        List<ResponsePost> result = postService.getAllMyPosts();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("test1", result.get(0).text());
        assertEquals("test2", result.get(1).text());
        verify(fishRepository, times(1)).findByAppUserId(userId);
        verify(postRepository, times(1)).findByFishId(fish1.getId());
        verify(postRepository, times(1)).findByFishId(fish2.getId());
    }


    @Test
    @DisplayName("save creates a new post with correct values")
    void saveCreatesANewPostWithCorrectValues() {
        CreatePost createPost = new CreatePost("saveTest", "Stockholm", fishTest.getId());
        Point<G2D> point = null;
        Post post = PostMapper.map(createPost, fishTest, null);
        when(fishRepository.findById(fishTest.getId())).thenReturn(Optional.of(fishTest));
        when(geoService.geocodeCity("Stockholm")).thenReturn(new Point<>(
                new G2D(18.0686, 59.3293), CoordinateReferenceSystems.WGS84));

        postService.save(createPost);
        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository, times(1)).save(postCaptor.capture());
        Post capturedPost = postCaptor.getValue();

        assertEquals(post.getText(), capturedPost.getText());
        assertEquals(post.getFishid(), capturedPost.getFishid());
    }

    @Test
    @DisplayName("save throws NotFound when fishRep can not find fish")
    void saveThrowsNotFoundWhenFishRepCanNotFindFish() {
        CreatePost createPost = new CreatePost("saveTest", "Stockholm", fishTest.getId());

        when(fishRepository.findById(fishTest.getId())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                postService.save(createPost));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Fish not found", exception.getReason());
    }

    @Test
    @DisplayName("update Updates posts text with DTOs text and rep.update is only called once")
    void updateUpdatesPostsTextWithDtOsTextAndRepUpdateIsOnlyCalledOnce() {

        UpdatePost updatePost = new UpdatePost("updateTest", null);
        when(postRepository.findById(1)).thenReturn(Optional.of(postTest));

        postService.update(postTest.getId(), updatePost);

        ArgumentCaptor<String> textCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(postRepository, times(1)).update(textCaptor.capture(), idCaptor.capture());

        assertEquals("updateTest", textCaptor.getValue());
        assertEquals(1, idCaptor.getValue());
    }

    @Test
    @DisplayName("update throws NotFound when postRep can not find post")
    void updateThrowsNotFoundWhenPostRepCanNotFindPost() {
        UpdatePost updatePost = new UpdatePost("updateTest", null);
        when(postRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                postService.update(1, updatePost));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Post not found", exception.getReason());
    }

    @Test
    @DisplayName("delete removes the post when it exists")
    void deleteRemovesThePostWhenItExists() {
        when(postRepository.findById(1)).thenReturn(Optional.of(postTest));
        postService.delete(postTest.getId());
        verify(postRepository, times(1)).delete(postTest);
    }

    @Test
    @DisplayName("delete throws NotFound when postRep can not find Post")
    void deleteThrowsNotFoundWhenPostRepCanNotFindPost() {
        when(postRepository.findById(1)).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                postService.delete(1));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Post not found", exception.getReason());

    }

    @Test
    @DisplayName("saveAndReturn saves and returns the created post")
    void saveAndReturnSavesAndReturnsPost() {
        CreatePost createPost = new CreatePost("saveReturnTest", "Göteborg", fishTest.getId());
        Point<G2D> point = new Point<>(new G2D(11.9746, 57.7089), CoordinateReferenceSystems.WGS84);

        when(fishRepository.findById(fishTest.getId())).thenReturn(Optional.of(fishTest));
        when(geoService.geocodeCity("Göteborg")).thenReturn(point);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Post result = postService.saveAndReturn(createPost);

        assertNotNull(result);
        assertEquals("saveReturnTest", result.getText());
        assertEquals("Göteborg", result.getCityName());
        assertEquals(point, result.getCoordinate());
        assertEquals(fishTest, result.getFishid());
    }

    @Test
    @DisplayName("findByFishId returns all posts related to fishId")
    void findByFishIdReturnsPostsForGivenFishId() {
        int fishId = 1;

        Post post1 = new Post();
        post1.setId(10);
        post1.setText("First Post");
        post1.setFishid(fishTest);

        Post post2 = new Post();
        post2.setId(11);
        post2.setText("Second Post");
        post2.setFishid(fishTest);

        when(postRepository.findByFishId(fishId)).thenReturn(List.of(post1, post2));

        List<ResponsePost> results = postService.findByFishId(fishId);

        assertEquals(2, results.size());
        assertEquals("First Post", results.get(0).text());
        assertEquals("Second Post", results.get(1).text());
    }


}