package org.example.fishtank.controller;

import org.example.fishtank.model.dto.fishDto.CreateFish;
import org.example.fishtank.model.dto.fishDto.ResponseFish;
import org.example.fishtank.model.dto.fishDto.UpdateFish;
import org.example.fishtank.model.entity.Fish;
import org.example.fishtank.repository.FishRepository;
import org.example.fishtank.service.FishService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class FishController {

    private final FishService fishService;
    private final FishRepository fishRepository;

    public FishController(FishService fishService, FishRepository fishRepository) {
        this.fishService = fishService;
        this.fishRepository = fishRepository;
    }

    @GetMapping("/my_fishes")
    String myFishes(Model model) {
        var fishList = fishService.getAllFish();
        model.addAttribute("fishList", fishList);
        return "my_fishes";
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
            @RequestParam("appuser") String appuser) {
        CreateFish createFish = new CreateFish(name, species, description, watertype, sex, appuser);
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
        System.out.println(updateFish + "Before update");
        fishService.update(id, updateFish);
        System.out.println(updateFish + "After update");
        return "redirect:/my_fishes/" + id;
    }

    @GetMapping("/update_fish/{id}")
    public String showUpdateFishForm(@PathVariable Integer id, Model model) {
        ResponseFish fish = fishService.findById(id);
        if (fish == null) {
            System.out.println("Fish not found for ID: " + id);}
        System.out.println("Fish found with ID: " + id);
        model.addAttribute("fish", fish);
        return "update_fish";
    }

    @PostMapping("/delete_fish/{id}")
    public String deleteFish(@PathVariable Integer id) {
        fishService.delete(id);
        return "redirect:/my_fishes";
    }

}
