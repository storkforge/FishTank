package org.example.fishtank.controller;

import org.example.fishtank.model.dto.postDto.CreatePost;
import org.example.fishtank.model.dto.postDto.ResponsePost;
import org.example.fishtank.model.dto.postDto.ResponsePostList;
import org.example.fishtank.model.dto.postDto.UpdatePost;
import org.example.fishtank.service.FishService;
import org.example.fishtank.service.GeoService;
import org.example.fishtank.service.PostService;
import org.example.fishtank.util.Haversine;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@Controller
public class PostController {

    private final GeoService geoService;
    private final PostService postService;
    private final FishService fishService;

    public PostController(GeoService geoService, PostService postService, FishService fishService) {
        this.geoService = geoService;
        this.postService = postService;
        this.fishService = fishService;
    }


    @ResponseBody
    @GetMapping(value = "/forum_rough/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponsePost getPostRoughById(@PathVariable Integer id) {
        return postService.findById(id);
    }

    @ResponseBody
    @GetMapping(value = "/forum_rough", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponsePostList getPostsRough() {
        return new ResponsePostList(postService.getAllPost());
    }


    @GetMapping("/add_post")
    public String showAddPostForm(Model model) {
        model.addAttribute("fishList", fishService.getAllFish());
        return "add_post";
    }

    @PostMapping("/add_post")
    public String addPost(
            @RequestParam("text") String text,
            @RequestParam("cityName") String cityName,
            @RequestParam("fishId") Integer fishId) {
        CreatePost createPost = new CreatePost(text, cityName, fishId);
        postService.save(createPost);
        return "redirect:/forum";
    }

    @GetMapping("/update_post/{id}")
    public String showUpdatePostForm(@PathVariable Integer id, Model model) {
        ResponsePost post = postService.findById(id);
        model.addAttribute("post", post);
        return "update_post";
    }

    @PostMapping("/update_post/{id}")
    public String updatePost(
            @PathVariable Integer id,
            @RequestParam("text") String text,
            @RequestParam(value = "cityName", required = false) String cityName) {
        UpdatePost updatePost = new UpdatePost(text, cityName);
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
        var post = postService.findById(id);
        var fish = fishService.findById(post.fishId());
        model.addAttribute("fish", fish);
        model.addAttribute("post", post);
        return "post";
    }

    @GetMapping("/my_posts")
    String showMyPosts(Model model) {
        var postList = postService.getAllPost();
        var fishList = postList.stream()
                .map(post -> fishService.findById(post.fishId()))
                .filter(Objects::nonNull)
                .toList();
        model.addAttribute("postList", postList);
        model.addAttribute("fishList", fishList);
        return "my_posts";
    }

    @GetMapping("/forum")
    public String showForum(@RequestParam(required = false) String location,
                            @RequestParam(required = false, defaultValue = "25") double radius,
                            Model model) {
        List<ResponsePost> allPosts = postService.getAllPost();
        List<ResponsePost> filteredPosts = allPosts;

        if (location != null && !location.isBlank()) {
            Point<G2D> center = geoService.geocodeCity(location);
            if (center != null) {
                filteredPosts = allPosts.stream()
                        .filter(p -> p.latitude() != null && p.longitude() != null)
                        .filter(p -> {
                            double distanceKm = Haversine.distance(
                                    center.getPosition().getLat(),
                                    center.getPosition().getLon(),
                                    p.latitude(),
                                    p.longitude());
                            return distanceKm <= radius;
                        })
                        .toList();
            }
        }

        model.addAttribute("postList", filteredPosts);
        model.addAttribute("fishList", filteredPosts.stream()
                .map(p -> fishService.findById(p.fishId()))
                .toList());
        model.addAttribute("location", location);
        model.addAttribute("radius", radius);

        return "forum";
    }

    @GetMapping("/forum/map")
    public String showPostMap(Model model) {
        List<ResponsePost> allPosts = postService.getAllPost();
        model.addAttribute("postList", allPosts);
        return "forum_map";
    }
}
