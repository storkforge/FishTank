package org.example.fishtank.controller;

import org.example.fishtank.model.entity.AppUser;
import org.example.fishtank.repository.UserRepository;
import org.example.fishtank.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LogInController {

    private final AppUserService appUserService;

    private final UserRepository userRepository;

    public LogInController(AppUserService appUserService, UserRepository userRepository) {
        this.appUserService = appUserService;
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("form") AppUser appUser, Model model) {
        appUser = userRepository.findByNameAndPasswordHash(appUser.getEmail(), appUser.getPasswordHash());
        if (appUser == null) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
        else {
            return "redirect:/signup";
        }

    }

}
