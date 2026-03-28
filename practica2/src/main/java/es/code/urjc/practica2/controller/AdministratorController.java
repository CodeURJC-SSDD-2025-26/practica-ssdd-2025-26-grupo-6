package es.code.urjc.practica2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdministratorController {
    @GetMapping("/administrator")
    public String administrator(Model model) {
        return "administrator";
    }

    @GetMapping("/includeMovie")
    public String includeMovie(Model model) {
        return "includeMovie";
    }

    @GetMapping("/includeSerie")
    public String includeSerie(Model model) {
        return "includeSerie";
    }

    @GetMapping("/modifyMovie")
    public String modifyMovie(Model model) {
        return "modifyMovie";
    }

    @GetMapping("/modifySeries")
    public String modifySeries(Model model) {
        return "modifySeries";
    }
}
