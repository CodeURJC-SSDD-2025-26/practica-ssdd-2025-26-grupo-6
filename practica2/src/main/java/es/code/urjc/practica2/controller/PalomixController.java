package es.code.urjc.practica2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PalomixController {
    @GetMapping("/aboutUs")
    public String aboutUs(Model model) {
        return "aboutUs";
    }

    @GetMapping("/cookies")
    public String cookies(Model model) {
        return "cookies";
    }

    @GetMapping("/frequentlyAskedQuestions")
    public String frequentlyAskedQuestions(Model model) {
        return "frequentlyAskedQuestions";
    }

    @GetMapping("/legalAdvise")
    public String legalAdvise(Model model) {
        return "legalAdvise";
    }
}
