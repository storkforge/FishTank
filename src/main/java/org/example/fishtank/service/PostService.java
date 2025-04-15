package org.example.fishtank.service;

import jakarta.transaction.Transactional;
import org.example.fishtank.model.dto.fishDto.ResponseFish;
import org.example.fishtank.model.dto.postDto.CreatePost;
import org.example.fishtank.model.dto.postDto.ResponsePost;
import org.example.fishtank.model.dto.postDto.UpdatePost;
import org.example.fishtank.model.entity.*;
import org.example.fishtank.model.mapper.PostMapper;
import org.example.fishtank.repository.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {

    PostRepository postRepository;
    FishRepository fishRepository;

    public PostService(PostRepository postRepository, FishRepository fishRepository) {
        this.fishRepository = fishRepository;
        this.postRepository = postRepository;
    }

    @Cacheable(value = "post", key = "#id")
    public ResponsePost findById(Integer id) {
        return postRepository.findById(id)
                .map(PostMapper::map)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    }

    @Cacheable(value = "post", key = "#id")
    public ResponsePost findByMyId(Integer id) {
        var currentUserId = CurrentUser.getId();
        var fishList = fishRepository.findByAppUserId(currentUserId);
        return fishList.stream()
                .flatMap(fish -> postRepository.findByFishId(fish.getId()).stream())
                .filter(post -> post.getId().equals(id))
                .map(PostMapper::map)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found or you do not have access"));
    }

    @Cacheable("allPost")
    public List<ResponsePost> getAllPost() {
        return postRepository.findAll()
                .stream()
                .map(PostMapper::map)
                .filter(Objects::nonNull)
                .toList();
    }

    public List<ResponsePost> findByFishId(Integer id) {
        List<Post> posts = postRepository.findByFishId(id);
        return posts.stream()
                .map(PostMapper::map)
                .toList();
    }

    @Cacheable("myPost")
    public List<ResponsePost> getAllMyPosts() {
        var fishList = fishRepository.findByAppUserId(CurrentUser.getId());
        return fishList.stream()
                .flatMap(fish -> {
                    var posts = postRepository.findByFishId(fish.getId());
                    return posts.stream();
                })
                .map(PostMapper::map)
                .filter(Objects::nonNull)
                .toList();
    }

    @CacheEvict(value = {"post", "allPost", "myPost"}, allEntries = true)
    public void save(CreatePost createPost) {
        Fish fish = fishRepository.findById(createPost.fishId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fish not found"));
        Post post = PostMapper.map(createPost, fish);
        postRepository.save(post);
    }

    //save with return
    public Post saveAndReturn(CreatePost createPost) {
        Fish fish = fishRepository.findById(createPost.fishId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fish not found"));
        Post post = PostMapper.map(createPost, fish);
        return postRepository.save(post);
    }

    @CacheEvict(value = {"post", "allPost", "myPost"}, key = "#id", allEntries = true)
    public void update(int id, UpdatePost post) {
        Post oldPost = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        PostMapper.map(post, oldPost);
        postRepository.update(oldPost.getText(), oldPost.getId());
    }

    @CacheEvict(value = {"post", "allPost", "myPost"}, key = "#id", allEntries = true)
    public void delete(int id) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        postRepository.delete(post);
    }
}
