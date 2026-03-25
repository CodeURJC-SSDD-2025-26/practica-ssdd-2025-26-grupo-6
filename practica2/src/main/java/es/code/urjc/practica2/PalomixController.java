package es.code.urjc.practica2;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class PalomixController {
    @GetMapping("/")
    public String principal(Model model) {
        return "filmsLists";
    }
    
}
