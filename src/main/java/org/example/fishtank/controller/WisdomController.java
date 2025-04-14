package org.example.fishtank.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class WisdomController {

    @GetMapping("/wisdom")
    public String getWisdom() {
        return "wisdom";
    }
}
