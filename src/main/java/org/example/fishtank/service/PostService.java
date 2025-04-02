package org.example.fishtank.service;

import jakarta.transaction.Transactional;
import org.example.fishtank.model.dto.postDto.CreatePost;
import org.example.fishtank.model.dto.postDto.ResponsePost;
import org.example.fishtank.model.dto.postDto.UpdatePost;
import org.example.fishtank.model.entity.*;
import org.example.fishtank.model.mapper.PostMapper;
import org.example.fishtank.repository.*;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class PostService {

    PostRepository postRepository;
    FishRepository fishRepository;

    public PostService(PostRepository postRepository, FishRepository fishRepository) {
        this.fishRepository = fishRepository;
        this.postRepository = postRepository;
    }

    public ResponsePost findById(Integer id) {
        return postRepository.findById(id)
                .map(PostMapper::map)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public List<ResponsePost> getAllPost() {
        return postRepository.findAll()
                .stream()
                .map(PostMapper::map)
                .filter(Objects::nonNull)
                .toList();
    }


    public void save(CreatePost createPost) {
        Fish fish = fishRepository.findById(createPost.fishId()).orElseThrow(() ->
                new RuntimeException("Post not found"));

        Post post = PostMapper.map(createPost, fish);
        postRepository.save(post);
    }

    public void update(int id, UpdatePost post) {
        Post oldPost = postRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Post not found"));
        PostMapper.map(post, oldPost);
        postRepository.update(oldPost.getText(), oldPost.getId());
    }

    public void delete(int id) {
        var post = postRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Fish not found"));
        postRepository.delete(post);
    }

}
