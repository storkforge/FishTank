package org.example.fishtank.controller;

import org.example.fishtank.model.dto.postDto.ResponsePost;
import org.example.fishtank.model.dto.postDto.ResponsePostList;
import org.example.fishtank.service.FishService;
import org.example.fishtank.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;


@Controller
public class PostController {

    private final PostService postService;
    private final FishService fishService;

    public PostController(PostService postService, FishService fishService) {
        this.postService = postService;
        this.fishService = fishService;
    }

    @ResponseBody
    @GetMapping("/forum_rough/{id}")
    public ResponsePost getPostById(@PathVariable(name = "id") int id) {
        return postService.findById(id);
    }


    //Json
    @ResponseBody
    @GetMapping("/forum_rough")
    public ResponsePostList getPostsRough() {
        return new ResponsePostList(postService.getAllPost());
    }

    @GetMapping("/forum")
    String forum(Model model) {
        var postList = postService.getAllPost();
        var fishList = postList.stream()
                .map(post -> fishService.findById(post.fishId()))
                .filter(Objects::nonNull)
                .toList();
        model.addAttribute("postList", postList);
        model.addAttribute("fishList", fishList);
        return "forum";
    }
}
