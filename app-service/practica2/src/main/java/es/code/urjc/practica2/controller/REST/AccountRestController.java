package es.code.urjc.practica2.controller.REST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import es.code.urjc.practica2.mapper.AccountMapper;
import es.code.urjc.practica2.mapper.ListsMapper;
import es.code.urjc.practica2.mapper.MovieMapper;
import es.code.urjc.practica2.mapper.ReviewMapper;
import es.code.urjc.practica2.mapper.SerieMapper;
import es.code.urjc.practica2.service.AccountService;
import es.code.urjc.practica2.service.FilmographyService;
import es.code.urjc.practica2.service.ImageService;
import es.code.urjc.practica2.service.ListsService;
import es.code.urjc.practica2.service.ReviewService;

@RestController
public class AccountRestController {
    @Autowired
    private ListsService listsService;
    @Autowired
    private FilmographyService filmographyService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ImageService imageService;

    @Autowired 
    private AccountMapper accountMapper;
    @Autowired 
    private MovieMapper movieMapper;
    @Autowired 
    private SerieMapper serieMapper;
    @Autowired 
    private ReviewMapper reviewMapper;
    @Autowired 
    private ListsMapper listsMapper;


    
}
