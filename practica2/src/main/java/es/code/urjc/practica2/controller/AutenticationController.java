package es.code.urjc.practica2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AutenticationController {
    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/signUp")
    public String signUp(Model model) {
        return "signUp";
    }
}
