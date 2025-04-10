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

    private AppUserService appUserService;

    public LoginController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping()
    public String base(){

        var authenticationName = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("AuthenticationName in base: " + authenticationName);

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

    @PostMapping("/login")
    public String loginPost(@RequestParam("username") String username, @RequestParam("password") String password){

        System.out.println("PostMapping /login, username " + username + " password " + password);
        return "redirect:/";
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

        if (username == null || password == null || email == null) {
            model.addAttribute("error", "User data is incomplete!");
            return "signup";
        }

        if(appUserService.isNamePresentInDB(username)){
            model.addAttribute("error", "Username already exists!");
            return "signup";
        }

        String authenticationCode = "form_" + username;

        CreateAppUser createAppUser = new CreateAppUser(username, password, email, authenticationCode,"Standard");

        appUserService.saveToDB(createAppUser);

        return "redirect:/login";
    }
}


