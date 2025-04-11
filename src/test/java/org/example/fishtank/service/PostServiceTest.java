package org.example.fishtank.service;

import org.example.fishtank.model.dto.fishDto.ResponseFish;
import org.example.fishtank.model.dto.postDto.ResponsePost;
import org.example.fishtank.model.entity.Fish;
import org.example.fishtank.model.entity.Post;
import org.example.fishtank.repository.FishRepository;
import org.example.fishtank.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    PostService postService;

    @Mock
    PostRepository postRepository;

    ResponsePost responsePost;

    @Test
    void findById() {
        Fish fish = new Fish();
        fish.setId(1);
        fish.setName("testFish");
        fish.setDescription("testFishDescription");
        fish.setSpecies("testFishSpecies");
        fish.setWaterType();
        Post post = new Post();
        post.setId(1);
        post.setText("test");
        post.setFishid(fish);
        when(postRepository.findById(1)).thenReturn(Optional.of(post));

        when(postService.findById(1));
        assertEquals(responsePost, postService.findById(1));

    }

    @Test
    void getAllPost() {
    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}
