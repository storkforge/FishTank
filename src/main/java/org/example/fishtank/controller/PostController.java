package org.example.fishtank.controller;

import org.example.fishtank.model.dto.postDto.CreatePost;
import org.example.fishtank.model.dto.postDto.ResponsePost;
import org.example.fishtank.model.dto.postDto.ResponsePostList;
import org.example.fishtank.model.dto.postDto.UpdatePost;
import org.example.fishtank.service.FishService;
import org.example.fishtank.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


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

    @ResponseBody
    @GetMapping("/forum_rough")
    public ResponsePostList getPostsRough() {
        return new ResponsePostList(postService.getAllPost());
    }

    @GetMapping("/add_post")
    public String showAddPostForm(Model model) {
        model.addAttribute("fishList", fishService.getMyFish());
        return "add_post";
    }

    @PostMapping("/add_post")
    public String addPost(
            @RequestParam("text") String text,
            @RequestParam("fishId") Integer fishId) {
        CreatePost createPost = new CreatePost(text, fishId);
        postService.save(createPost);
        return "redirect:/forum";
    }

    @GetMapping("/update_post/{id}")
    public String showUpdatePostForm(@PathVariable Integer id, Model model) {
        ResponsePost post = postService.findByMyId(id);
        model.addAttribute("post", post);
        return "update_post";
    }

    @PostMapping("/update_post/{id}")
    public String updatePost(
            @PathVariable Integer id,
            @RequestParam("text") String text) {
        UpdatePost updatePost = new UpdatePost(text);
        postService.update(id, updatePost);
        return "redirect:/my_posts";
    }

    @PostMapping("/delete_post/{id}")
    public String deletePost(@PathVariable Integer id) {
        postService.delete(id);
        return "redirect:/my_posts";
    }

    @GetMapping("/my_posts/{id}")
    String postByID(Model model, @PathVariable Integer id) {
        var post = postService.findByMyId(id);
        var fish = fishService.findById(post.fishId());
        model.addAttribute("fish", fish);
        model.addAttribute("post", post);
        return "post";
    }

    @GetMapping("/my_posts")
    String showMyPosts(Model model) {
        var postList = postService.getAllMyPosts();
        var fishList = fishService.getFishByPost(postList);
        model.addAttribute("postList", postList);
        model.addAttribute("fishList", fishList);
        return "my_posts";
    }

    @GetMapping("/forum")
    String forum(Model model) {
        var postList = postService.getAllPost();
        var fishList = fishService.getFishByPost(postList);
        model.addAttribute("postList", postList);
        model.addAttribute("fishList", fishList);
        return "forum";
    }
}
