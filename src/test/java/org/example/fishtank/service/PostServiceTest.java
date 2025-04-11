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

    @Mock
    PostMapper postMapper;

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
    void getAllPost() {
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
    void save() {
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
    @DisplayName("update Updates posts text with DTOs text and rep.update is only called once")
    void updateUpdatesPostsTextWithDtOsTextAndRepUpdateIsOnlyCalledOnce() {

        UpdatePost updatePost = new UpdatePost("updateTest");
        when(postRepository.findById(1)).thenReturn(Optional.of(postTest));

        postService.update(postTest.getId(), updatePost);

        ArgumentCaptor<String> textCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(postRepository, times(1)).update(textCaptor.capture(), idCaptor.capture());

        assertEquals("updateTest", textCaptor.getValue());


    }


    @Test
    void delete() {
        when(postRepository.findById(1)).thenReturn(Optional.of(postTest));
        postService.delete(postTest.getId());
        verify(postRepository, times(1)).delete(postTest);
    }
}
