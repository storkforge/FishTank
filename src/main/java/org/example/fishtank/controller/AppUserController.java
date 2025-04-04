package org.example.fishtank.controller;

import org.example.fishtank.service.AppUserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppUserController {

    private AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

   /* @GetMapping()
    public String base(@AuthenticationPrincipal OAuth2User oAuth2User){

        String userNameAttribute = oAuth2User.getAttribute("login");
        appUserService.save(userNameAttribute);

        return "redirect:/home";
    }*/

    @GetMapping("/home")
    public String home(){
        return "home";
    }
}
