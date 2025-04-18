package org.example.fishtank.controller;

import org.example.fishtank.model.dto.appUserDto.UpdateAppUser;
import org.example.fishtank.model.dto.fishDto.CreateFish;
import org.example.fishtank.model.dto.fishDto.ResponseFish;
import org.example.fishtank.model.dto.fishDto.ResponseFishList;
import org.example.fishtank.model.dto.fishDto.UpdateFish;
import org.example.fishtank.service.AppUserService;
import org.example.fishtank.service.CurrentUser;
import org.example.fishtank.service.FishService;
import org.example.fishtank.service.ImageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;


@Controller
public class FishController {

    private final FishService fishService;
    private final ImageService imageService;
    private final AppUserService appUserService;


    public FishController(FishService fishService, ImageService imageService, AppUserService appUserService) {
        this.fishService = fishService;
        this.imageService = imageService;
        this.appUserService = appUserService;
    }

    @GetMapping("/my_fishes")
    String myFishes(Model model) {
        var fishList = fishService.getMyFish();
        model.addAttribute("fishList", fishList);
        return "my_fishes";
    }

    @ResponseBody
    @GetMapping("/my_fishes_rough/{id}")
    public ResponseFish myFishesById(@PathVariable(name = "id") int id) {
        return fishService.findMyFishById(id);
    }

    @ResponseBody
    @GetMapping("/my_fishes_rough")
    public ResponseFishList myFishesRough() {
        return new ResponseFishList(fishService.getMyFish());
    }

    @GetMapping("/add_fish")
    public String showAddFishForm(Model model) {
        var appuser = appUserService.getResponseAppUserById(CurrentUser.getId());
        model.addAttribute("appuser", appuser);
        return "add_fish";
    }

    @PostMapping("/add_fish")
    public String addFish(
            @RequestParam("name") String name,
            @RequestParam("species") String species,
            @RequestParam("description") String description,
            @RequestParam("watertype") String watertype,
            @RequestParam("sex") String sex,
            @RequestParam("fishImage") MultipartFile fishImage) throws IOException {
        String imageName = imageService.saveImage(fishImage);
        String appuser = appUserService.getResponseAppUserById(CurrentUser.getId()).name();
        CreateFish createFish = new CreateFish(name, species, description, watertype, sex, appuser, imageName);
        fishService.save(createFish);

        int userId = CurrentUser.getId();
        int fishCount = fishService.getFishCountByAppUser(CurrentUser.getId());

        if(fishCount > 5) {

            appUserService.updateAppUserToAccessPremium(userId);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetails updatedUserDetails = appUserService.loadUserByUsername(auth.getName());

            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                    updatedUserDetails,
                    auth.getCredentials(),
                    updatedUserDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }

        return "redirect:/my_fishes";
    }

    @GetMapping("/my_fishes/{id}")
    String fishById(Model model, @PathVariable Integer id) {
        var fish = fishService.findMyFishById(id);
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
        ResponseFish fish = fishService.findMyFishById(id);
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
