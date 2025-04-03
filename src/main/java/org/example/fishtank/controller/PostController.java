package org.example.fishtank.controller;

import org.example.fishtank.repository.FishRepository;
import org.example.fishtank.repository.PostRepository;
import org.example.fishtank.service.FishService;
import org.example.fishtank.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;
    private final FishService fishService;
    private final FishRepository fishRepository;

    public PostController(PostService postService, PostRepository postRepository, FishService fishService, FishRepository fishRepository) {
        this.postService = postService;
        this.postRepository = postRepository;
        this.fishService = fishService;
        this.fishRepository = fishRepository;
    }

    @GetMapping("/forum")
    String forum(Model model) {
        var postList = postService.getAllPost();
        var fishList = postList.stream()
                .map(post -> fishService.findById(post.fishId()))
                .toList();
        model.addAttribute("postList", postList);
        model.addAttribute("fishList", fishList);
        return "forum";
    }
}
