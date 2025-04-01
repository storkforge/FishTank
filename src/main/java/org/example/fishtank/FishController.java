package org.example.fishtank;

import org.example.fishtank.model.dto.fishDto.CreateFish;
import org.example.fishtank.model.entity.Fish;
import org.example.fishtank.model.mapper.FishMapper;
import org.example.fishtank.repository.FishRepository;
import org.example.fishtank.service.FishService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FishController {

    private final FishService fishService;
    private final FishRepository fishRepository;

    public FishController(FishService fishService, FishRepository fishRepository) {
        this.fishService = fishService;
        this.fishRepository = fishRepository;
    }

    @GetMapping("/my_fishes")
    String myFishes (Model model) {
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
            @RequestParam("appuser") String appuser)  {
        CreateFish createFish = new CreateFish(name, species, description, watertype, sex, appuser);
        fishService.save(createFish);
        return "my_fishes";
    }

}
