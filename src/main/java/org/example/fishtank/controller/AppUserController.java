package org.example.fishtank.controller;

import org.example.fishtank.service.AppUserService;
import org.example.fishtank.service.CurrentUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppUserController {

    private final CurrentUser currentUser;

    public AppUserController(CurrentUser currentUser) {
        this.currentUser = currentUser;

    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        var authentication = currentUser.getRole();
        var user = currentUser.getUsername();
        model.addAttribute("authentication", authentication);
        model.addAttribute("user", user);
        return "profile";
    }

}
