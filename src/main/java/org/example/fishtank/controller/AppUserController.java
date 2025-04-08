package org.example.fishtank.controller;

import org.example.fishtank.model.entity.AppUser;
import org.example.fishtank.service.AppUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class  AppUserController {

    private AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

//    @GetMapping()
//    public String base(@AuthenticationPrincipal OAuth2User oAuth2User){
//
//        String userNameAttribute = oAuth2User.getAttribute("login");
//
//        appUserService.save(userNameAttribute);
//
//        return "redirect:/home";
//    }

//    @GetMapping()
//    public String base(){
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication.getPrincipal() instanceof OAuth2User oauth2User) {
//
//            Map<String, Object> attributes = oauth2User.getAttributes();
//            //attributes.forEach((k, v) -> System.out.println(k + ": " + v));
//
//            if(attributes.containsKey("login")){
//                String gitUsername = oauth2User.getAttribute("login");
//                appUserService.save(gitUsername);
//                return "redirect:/home";
//            }
//        }
//
//        return "redirect:/login";
//    }

    @GetMapping()
    public String base(){

        var authenticationName = SecurityContextHolder.getContext().getAuthentication().getName();

        if(authenticationName.equals("anonymousUser")){
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

        System.out.println(username);
        System.out.println(password);

        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(){
        return "home";
    }

    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }

    @PostMapping("/signup")
    public String signupPost(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("email") String email){

        System.out.println("Signup username: " + username);
        System.out.println("Signup password: " + password);
        System.out.println("Signup email: " + email);

        return "redirect:/login";
    }
}
