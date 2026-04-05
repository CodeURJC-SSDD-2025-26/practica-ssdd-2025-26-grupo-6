package es.code.urjc.practica2.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.code.urjc.practica2.model.Account;
import es.code.urjc.practica2.model.Filmography;
import es.code.urjc.practica2.model.Lists;
import es.code.urjc.practica2.model.Movie;
import es.code.urjc.practica2.model.Review;
import es.code.urjc.practica2.model.Serie;
import es.code.urjc.practica2.service.AccountService;
import es.code.urjc.practica2.service.FilmographyService;
import es.code.urjc.practica2.service.ListsService;

@Controller
public class FilmographyController {
    @Autowired
    private FilmographyService filmographyService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ListsService listsService;

    @GetMapping("/filmsLists")
    public String filmsLists(Model model) {
        return "filmsLists";
    }

    @GetMapping("/principal") //Header
    public String principal(Model model) {
        return "principal";
    }

    @GetMapping("/lists") //Header
    public String lists(Model model) {
        return "lists";
    }

    @GetMapping("/review")
    public String review(Model model) {
        return "review";
    }

    @GetMapping("/series") //Header
    public String series(Model model) {
        return "series";
    }

    @GetMapping("/filmographies/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Filmography filmography = filmographyService.findById(id);

        model.addAttribute("filmography", filmography);
 
        // Check if it's a movie or a serie to show the correct information
        if (filmography instanceof Serie serie) {
            model.addAttribute("isSeries", true);
            model.addAttribute("serieDuration", serie.getSerieDuration());
        } else {
            Movie movie = (Movie) filmography;
            model.addAttribute("isSeries", false);
            model.addAttribute("movieDuration", movie.getMovieDuration());
        }
 
        // Stars
        List<Map<String, Object>> starsList = new ArrayList<>();
        float avg = filmography.getFilmographyAverageStars();
        model.addAttribute("averageStars", Math.round(avg * 100.0f) / 100.0f);
        for (int i = 1; i <= 5; i++) {
            Map<String, Object> star = new HashMap<>();
            float fill;
            if (avg >= i) {
                fill = 100f;
            } else if (avg > i - 1) {
                fill = (avg - (i - 1)) * 100f;
            } else {
                fill = 0f;
            }
            star.put("fillPercent", Math.round(fill));
            star.put("hasColor", fill > 0);
            starsList.add(star);
        }
        model.addAttribute("starsList", starsList);
 
        // Empty review
        model.addAttribute("review", new Review());
 
        // User lists
        Account currentUser = accountService.getCurrentUser();
        List<Map<String, Object>> userListsWithCheck = new ArrayList<>();
        if (currentUser != null) {
            for (Lists list : currentUser.getAccountLists()) {
                Map<String, Object> listMap = new HashMap<>();
                listMap.put("listsId", list.getListsId());
                listMap.put("listName", list.getListName());
                listMap.put("checked", list.getFilmographyList().contains(filmography));
                userListsWithCheck.add(listMap);
            }
        }
        model.addAttribute("userLists", userListsWithCheck);
 
        return "filmographyDetails";
    }
 
    // Updates which lists contain this filmography (add or remove based on checkboxes)
    @PostMapping("/filmographies/{id}/lists/update")
    @ResponseBody
    public void updateFilmographyLists(@PathVariable Long id, @RequestParam(required = false) List<Long> listIds) {
        Account currentUser = accountService.getCurrentUser();
        Filmography filmography = filmographyService.findById(id);
 
        for (Lists list : currentUser.getAccountLists()) {
            boolean isChecked = listIds != null && listIds.contains(list.getListsId());
            boolean alreadyContains = list.getFilmographyList().contains(filmography);
 
            if (isChecked && !alreadyContains) {
                list.getFilmographyList().add(filmography);
                listsService.save(list);
            } else if (!isChecked && alreadyContains) {
                list.getFilmographyList().remove(filmography);
                listsService.save(list);
            }
        }
    }
 
    // Creates a new empty list for the current user and returns it as JSON
    @PostMapping("/filmographies/{id}/lists/new")
    @ResponseBody
    public Map<String, Object> createFilmographyList(@PathVariable Long id, @RequestParam String newListName) {
        Account currentUser = accountService.getCurrentUser();
 
        Lists newList = new Lists(newListName.trim(), new ArrayList<>());
        newList.setListOwner(currentUser);
        listsService.save(newList);
        currentUser.getAccountLists().add(newList);
        accountService.save(currentUser);
 
        Map<String, Object> result = new HashMap<>();
        result.put("listsId", newList.getListsId());
        result.put("listName", newList.getListName());
        return result;
    }
}
