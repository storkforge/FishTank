package org.example.fishtank.service;

import org.example.fishtank.model.dto.postDto.CreatePost;
import org.example.fishtank.model.dto.postDto.ResponsePost;
import org.example.fishtank.model.dto.postDto.UpdatePost;
import org.example.fishtank.model.entity.*;
import org.example.fishtank.model.mapper.PostMapper;
import org.example.fishtank.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    FishRepository fishRepository;

    @Mock
    PostRepository postRepository;


    Post postTest = new Post();
    Fish fishTest = new Fish();
    WaterType waterTypeTest = new WaterType();

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
    }

    @Test
    @DisplayName("FindById returns correct responsePost with correct values")
    void findByIdReturnsCorrectResponsePostWithCorrectValues() {
        ResponsePost responsePost = new ResponsePost(1, "test", 1);

        when(postRepository.findById(1)).thenReturn(Optional.of(postTest));
        assertEquals(responsePost, postService.findById(1));
    }

    @Test
    @DisplayName("NotFound is thrown when findById can not find the post")
    void notFoundIsThrownWhenFindByIdCanNotFindThePost() {
        when(postRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            postService.findById(1);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Post not found", exception.getReason());
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
        ResponsePost responsePost = new ResponsePost(1, "test", 1);
        ResponsePost responsePost2 = new ResponsePost(2, "test2", 1);

        responsePosts.add(responsePost);
        responsePosts.add(responsePost2);

        posts.add(postTest);
        posts.add(postTest2);

        when(postRepository.findAll()).thenReturn(posts);
        assertEquals(responsePosts, postService.getAllPost());
    }

    @Test
    @DisplayName("save creates a new post with correct values")
    void saveCreatesANewPostWithCorrectValues() {
        CreatePost createPost = new CreatePost("saveTest", fishTest.getId());
        Post post = PostMapper.map(createPost, fishTest);
        when(fishRepository.findById(fishTest.getId())).thenReturn(Optional.of(fishTest));

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
        CreatePost createPost = new CreatePost("saveTest", fishTest.getId());

        when(fishRepository.findById(fishTest.getId())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            postService.save(createPost);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Fish not found", exception.getReason());
    }

    @Test
    @DisplayName("update Updates posts text with DTOs text and rep.update is only called once")
    void updateUpdatesPostsTextWithDtOsTextAndRepUpdateIsOnlyCalledOnce() {

        UpdatePost updatePost = new UpdatePost("updateTest");
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
        UpdatePost updatePost = new UpdatePost("updateTest");
        when(postRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            postService.update(1, updatePost);
        });

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
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            postService.delete(1);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Post not found", exception.getReason());

    }

}
