package es.code.urjc.practica2.controller.REST;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.code.urjc.practica2.dto.FilmographyDto;
import es.code.urjc.practica2.dto.HomeResponseDto;
import es.code.urjc.practica2.dto.ListsDto;
import es.code.urjc.practica2.dto.MovieDto;
import es.code.urjc.practica2.dto.SerieDto;
import es.code.urjc.practica2.mapper.AccountMapper;
import es.code.urjc.practica2.mapper.FilmographyMapper;
import es.code.urjc.practica2.mapper.ListsMapper;
import es.code.urjc.practica2.mapper.MovieMapper;
import es.code.urjc.practica2.mapper.ReviewMapper;
import es.code.urjc.practica2.mapper.SerieMapper;
import es.code.urjc.practica2.model.Filmography;
import es.code.urjc.practica2.model.Lists;
import es.code.urjc.practica2.model.Movie;
import es.code.urjc.practica2.model.Serie;
import es.code.urjc.practica2.service.AccountService;
import es.code.urjc.practica2.service.FilmographyService;
import es.code.urjc.practica2.service.ListsService;
import es.code.urjc.practica2.service.ReviewService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api")
public class FilmographyRestController {
    @Autowired
    private ListsService listsService;
    @Autowired
    private FilmographyService filmographyService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private AccountService accountService;

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
    @Autowired
    private FilmographyMapper filmographyMapper;

    @GetMapping("/principal")   //Return ok and list of movies
    public ResponseEntity<HomeResponseDto> getPrincipalData() {

        List<MovieDto> moviesDTO = movieMapper.toDTOs(filmographyService.findTop10MoviesByYear());
        List<Map<String, Object>> sections = listsService.getMovieSections();
        HomeResponseDto response = new HomeResponseDto(moviesDTO, sections);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filmographies/{id}")  // return ok and detail of film
    public ResponseEntity<FilmographyDto> detail(@PathVariable long id) {
        return ResponseEntity.ok(filmographyMapper.toDTO(filmographyService.findById(id)));
    }

    
    

}
