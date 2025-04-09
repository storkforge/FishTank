package org.example.fishtank.controller;

import org.example.fishtank.model.dto.fishDto.CreateFish;
import org.example.fishtank.model.dto.fishDto.ResponseFish;
import org.example.fishtank.model.dto.fishDto.ResponseFishList;
import org.example.fishtank.model.dto.fishDto.UpdateFish;
import org.example.fishtank.repository.FishRepository;
import org.example.fishtank.service.FishService;
import org.example.fishtank.service.ImageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;


@Controller
public class FishController {

    private final FishService fishService;
    private final FishRepository fishRepository;
    private final ImageService imageService;

    public FishController(FishService fishService, FishRepository fishRepository, ImageService imageService) {
        this.fishService = fishService;
        this.fishRepository = fishRepository;
        this.imageService = imageService;
    }

    @GetMapping("/my_fishes")
    String myFishes(Model model) {
        var fishList = fishService.getAllFish();
        model.addAttribute("fishList", fishList);
        return "my_fishes";
    }

    @ResponseBody
    @GetMapping("/my_fishes_rough/{id}")
    public ResponseFish myFishesById(@PathVariable(name = "id") int id) {
        return fishService.findById(id);
    }

    @ResponseBody
    @GetMapping("/my_fishes_rough")
    public ResponseFishList myFishesRough() {
        return new ResponseFishList(fishService.getAllFish());
    }

    @GetMapping("/add_fish")
    public String showAddFishForm() {
        return "add_fish";
    }

    @PostMapping("/add_fish")
    public String addFish(
            @RequestParam("name") String name,
            @RequestParam("species") String species,
            @RequestParam("description") String description,
            @RequestParam("watertype") String watertype,
            @RequestParam("sex") String sex,
            @RequestParam("appuser") String appuser,
            @RequestParam("fishImage") MultipartFile fishImage) throws IOException {
        String imageName = imageService.saveImage(fishImage);
        CreateFish createFish = new CreateFish(name, species, description, watertype, sex, appuser, imageName);
        fishService.save(createFish);
        return "redirect:/my_fishes";
    }

    @GetMapping("/my_fishes/{id}")
    String fishById(Model model, @PathVariable Integer id) {
        var fish = fishService.findById(id);
        model.addAttribute("fish", fish);
        return "fish";
    }

    @PostMapping("/update_fish/{id}")
    public String updateFish(
            @PathVariable Integer id,
            @RequestParam("name") String name,
            @RequestParam("description") String description) {
        UpdateFish updateFish = new UpdateFish(name, description);
        fishService.update(id, updateFish);
        return "redirect:/my_fishes/" + id;
    }

    @GetMapping("/update_fish/{id}")
    public String showUpdateFishForm(@PathVariable Integer id, Model model) {
        ResponseFish fish = fishService.findById(id);
        if (fish == null) {
            System.out.println("Fish not found for ID: " + id);
        }
        model.addAttribute("fish", fish);
        return "update_fish";
    }

    @PostMapping("/delete_fish/{id}")
    public String deleteFish(@PathVariable Integer id) {
        fishService.delete(id);
        return "redirect:/my_fishes";
    }

    @GetMapping("/my_fishes/images/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) {
        try {
            if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
                throw new IllegalArgumentException("Invalid filename");
            }
            Path filePath = imageService.getImagePath(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                MediaType mediaType;
                if (filename.toLowerCase().endsWith(".png")) {
                    mediaType = MediaType.IMAGE_PNG;
                } else if (filename.toLowerCase().endsWith(".gif")) {
                    mediaType = MediaType.IMAGE_GIF;
                } else {
                    mediaType = MediaType.IMAGE_JPEG;
                }
                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
