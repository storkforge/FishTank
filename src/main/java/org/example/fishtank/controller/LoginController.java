package org.example.fishtank.controller;

import org.example.fishtank.model.dto.appUserDto.CreateAppUser;
import org.example.fishtank.model.entity.AppUser;
import org.example.fishtank.repository.UserRepository;
import org.example.fishtank.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;

@Controller
public class LoginController {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("appUser", new AppUser());
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("appUser") AppUser appUser, Model model) {
        AppUser storedUser = userRepository.findByEmail(appUser.getEmail());

        if (storedUser == null) {
            model.addAttribute("error", "E-postadressen finns inte");
            return "login";
        }

        if (!appUser.getPasswordHash().equals(storedUser.getPasswordHash())) {
            model.addAttribute("error", "Felaktigt lösenord, försök igen");
            return "login";
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(storedUser, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        return "redirect:/home";
    }


    /*
    @PostMapping("/login")
    public String loginUser(@RequestParam("email") String email,
                            @RequestParam("passwordHash") String passwordHash) {

        System.out.println("Email mottagen: " + email);
        System.out.println("Password mottagen: " + passwordHash);
        AppUser storedUser = userRepository.findByEmail(email);


        if (storedUser == null) {
            return "login";
        }
        System.out.println("Lösenord i databasen: " + storedUser.getPasswordHash());
        if (!passwordHash.equals(storedUser.getPasswordHash())) {
            return "login";
        }

        return "redirect:/home";
    }*/

    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        AppUser appUser = new AppUser();
        System.out.println("Passing user to Thymeleaf: " + appUser);
        model.addAttribute("appUser", appUser);
        return "signup";
    }

    @PostMapping("/signup")
    public String signUpUser(@ModelAttribute("appUser") CreateAppUser appUser, Model model) {
        System.out.println("Received user: " + appUser);
        if (appUser.name() == null || appUser.email() == null || appUser.passwordHash() == null) {
                model.addAttribute("error", "User data is incomplete!");
                return "signup";
            }
            appUserService.save(appUser);
            return "redirect:/login";
        }
    }
