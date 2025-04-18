package org.example.fishtank.controller;

import org.example.fishtank.model.dto.fishDto.ResponseFish;
import org.example.fishtank.service.FishService;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.Map;

@Controller
public class ChatController {

    private final OpenAiChatModel chatModel;
    private FishService fishService;

    @Autowired
    public ChatController(OpenAiChatModel chatModel, FishService fishService) {
        this.chatModel = chatModel;
        this.fishService = fishService;
    }

    @GetMapping("/recommendations/{id}")
    public String recommendById(@PathVariable Integer id, Model model) {

        ResponseFish responseFish = fishService.findById(id);

         String fishName = responseFish.species();

        String toysPrompt = "Suggest short and concise exactly one engaging toy for a '" + fishName + "'.";
        String foodPrompt = "Suggest short and concise exactly one nutritious food suitable for a '" + fishName + "'.";
        String aquariumPrompt = "Suggest short and concise exactly one ideal aquarium setup for a '" + fishName + "'.";

        String toyRecommendation = chatModel.call(toysPrompt);
        String foodRecommendation = chatModel.call(foodPrompt);
        String aquariumRecommendation = chatModel.call(aquariumPrompt);

        model.addAttribute("fish", responseFish);

        model.addAttribute("recommendations", Map.of(
                "toys", toyRecommendation,
                "food", foodRecommendation,
                "aquarium", aquariumRecommendation
        ));

        return "recommendations";
    }

    @GetMapping("/healthcare/{id}")
    public String healthcare(@PathVariable Integer id, Model model) {
        ResponseFish responseFish = fishService.findById(id);

        String fishName = responseFish.species();

        String healthcarePrompt = "Give short and concise fish healthcare tips for a '" + fishName + " without acting like an ai'.";

        String healthcareRecommendation = chatModel.call(healthcarePrompt);

        model.addAttribute("fish", responseFish);

        model.addAttribute("healthcare", Map.of(
                "healthcare", healthcareRecommendation
        ));

        return "healthcare";
    }
}


