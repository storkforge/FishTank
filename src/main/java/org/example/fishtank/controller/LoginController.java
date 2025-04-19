package org.example.fishtank.controller;

import org.example.fishtank.model.dto.appUserDto.CreateAppUser;
import org.example.fishtank.service.AppUserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final AppUserService appUserService;

    public LoginController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping()
    public String base(){

        var authenticationName = SecurityContextHolder.getContext().getAuthentication().getName();

        if(authenticationName.equals("anonymousUser")){
            System.out.println("Authentication says anonymous user. Redirecting to login again.");
            return "redirect:/login";
        }

        return "redirect:/home";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/signup")
    public String showSignUpForm() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signUpUser(@RequestParam("username") String username,
                             @RequestParam("password") String password,
                             @RequestParam("email") String email,
                             Model model) {

        String authenticationCode = "form_" + username;

        if (username == null || password == null || email == null ||
        username.isBlank() || password.isBlank() || email.isBlank()) {

            model.addAttribute("error", "User data is incomplete!");
            return "signup";
        }

        if(appUserService.isAppUserAuthenticationCodePresentInDB(authenticationCode)){

            model.addAttribute("error", "Username already exists!");
            return "signup";
        }

        CreateAppUser createAppUser = new CreateAppUser(username, password, email, authenticationCode, AppUserService.ACCESS_STANDARD);

        appUserService.save(createAppUser);

        return "redirect:/login";
    }
}


