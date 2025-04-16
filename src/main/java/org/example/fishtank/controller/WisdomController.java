package org.example.fishtank.controller;

import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class WisdomController {

    private final OpenAiChatModel chatModel;

    @Autowired
    public WisdomController(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @PreAuthorize("hasRole('ROLE_Premium')")
    @GetMapping("/wisdom")
    public String generate(@RequestParam(value = "message", defaultValue = "Tell me a magical wisdom based on the category of a random fish species") String message, Model model) {

        var message1 = new UserMessage(message);
        var message2 = new SystemMessage("Answer short and concise. Don't start with any info about you being an ai. Start the answer directly.");
        String response = chatModel.call(message1, message2);

        model.addAttribute("generation", response);

        return "wisdom";
    }
}
