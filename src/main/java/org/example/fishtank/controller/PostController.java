package org.example.fishtank.controller;

import org.example.fishtank.repository.FishRepository;
import org.example.fishtank.repository.PostRepository;
import org.example.fishtank.service.FishService;
import org.example.fishtank.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;


@Controller
public class PostController {

    private final PostService postService;
    private final FishService fishService;

    public PostController(PostService postService, FishService fishService) {
        this.postService = postService;
        this.fishService = fishService;
    }

    @GetMapping("/forum")
    String forum(Model model) {
        var postList = postService.getAllPost();
        var fishList = postList.stream()
                .map(post -> fishService.findById(post.fishId()))
                .filter(Objects::nonNull)
                .toList();
        if (postList.size() != fishList.size())
            throw new RuntimeException("post list size does not match fish list size");

        model.addAttribute("postList", postList);
        model.addAttribute("fishList", fishList);
        return "forum";
    }
}
