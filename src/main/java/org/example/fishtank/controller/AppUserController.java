package org.example.fishtank.controller;

import org.example.fishtank.service.CurrentUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class  AppUserController {

    @GetMapping("/home")
    public String home(){
        return "home";
    }

}
