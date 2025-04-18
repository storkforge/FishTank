package org.example.fishtank.service;

import jakarta.transaction.Transactional;
import org.example.fishtank.model.dto.postDto.CreatePost;
import org.example.fishtank.model.dto.postDto.ResponsePost;
import org.example.fishtank.model.dto.postDto.UpdatePost;
import org.example.fishtank.model.entity.Fish;
import org.example.fishtank.model.entity.Post;
import org.example.fishtank.model.mapper.PostMapper;
import org.example.fishtank.repository.FishRepository;
import org.example.fishtank.repository.PostRepository;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class PostService {


    private static final Logger logger = LoggerFactory.getLogger(FishService.class);


    PostRepository postRepository;
    FishRepository fishRepository;
    GeoService geoService;

    public PostService(PostRepository postRepository, FishRepository fishRepository, GeoService geoService) {
        this.fishRepository = fishRepository;
        this.postRepository = postRepository;
        this.geoService = geoService;
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

    @Cacheable(value = "postByFish", key = "#id")
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

    @CacheEvict(value = {"post", "allPost", "myPost","postByFish","fish","myFish","allFish"}, allEntries = true)
    public void save(CreatePost createPost) {
        Fish fish = fishRepository.findById(createPost.fishId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fish not found"));

        Point<G2D> point = null;
        if (createPost.cityName() != null && !createPost.cityName().isBlank()) {
            point = geoService.geocodeCity(createPost.cityName());
        }

        Post post = PostMapper.map(createPost, fish, point);
        postRepository.save(post);
    }

    @CacheEvict(value = {"post", "allPost", "myPost", "postByFish", "fish","myFish","allFish"}, allEntries = true)
    public Post saveAndReturn(CreatePost createPost) {
        Fish fish = fishRepository.findById(createPost.fishId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fish not found"));

        Point<G2D> point = null;
        if (createPost.cityName() != null && !createPost.cityName().isBlank()) {
            point = geoService.geocodeCity(createPost.cityName());
        }

        Post post = PostMapper.map(createPost, fish, point);
        return postRepository.save(post);
    }

    @CacheEvict(value = {"post", "allPost", "myPost","postByFish", "fish","myFish","allFish"}, key = "#id", allEntries = true)
    public void update(int id, UpdatePost post) {
        Post oldPost = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        PostMapper.map(post, oldPost);
        postRepository.update(oldPost.getText(), oldPost.getId());
    }

    @CacheEvict(value = {"post", "allPost", "myPost","postByFish", "fish","myFish","allFish"}, key = "#id", allEntries = true)
    public void delete(int id) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        logger.info("Deleting post with id: {}", id);
        postRepository.delete(post);
        logger.info("Deleted post with id: {}", id);
    }
}
