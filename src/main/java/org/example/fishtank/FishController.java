package org.example.fishtank;

import org.example.fishtank.service.FishService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FishController {

    private final FishService fishService;

    public FishController(FishService fishService) {
        this.fishService = fishService;
    }

    @GetMapping("/my_fishes/all")
    String getAllDailyWisdom (Model model){
        var fishList = fishService.getAllFish();
        model.addAttribute("fishList", fishList);
        return "fishList";
    }
}
