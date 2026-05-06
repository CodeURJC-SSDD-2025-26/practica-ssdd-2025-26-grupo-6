package es.code.urjc.practica2.controller.rest;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.code.urjc.practica2.dto.FilmographyDto;
import es.code.urjc.practica2.dto.HomeResponseDto;
import es.code.urjc.practica2.dto.ListsDto;
import es.code.urjc.practica2.dto.MovieDto;
import es.code.urjc.practica2.dto.ReviewDto;
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
@RequestMapping("/api/v1")
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

    @GetMapping("/principal")
    public ResponseEntity<HomeResponseDto> getPrincipalData() {
        List<MovieDto> moviesDTO = movieMapper.toDTOs(filmographyService.findTop10MoviesByYear());
        
        // Convert movie sections: replace Movie entities with MovieDtos
        List<Map<String, Object>> sections = listsService.getMovieSections()
            .stream()
            .map(section -> {
                Map<String, Object> converted = new HashMap<>(section);
                List<Movie> movies = (List<Movie>) section.get("movies");
                converted.put("movies", movieMapper.toDTOs(movies));
                return converted;
            })
            .toList();

        HomeResponseDto response = new HomeResponseDto(moviesDTO, sections);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/series")
    public ResponseEntity<List<Map<String, Object>>> getSeriesData() {
        return ResponseEntity.ok(
            listsService.getSeriesSections()
                .stream()
                .map(section -> {
                    Map<String, Object> converted = new HashMap<>(section);
                    List<Serie> series = (List<Serie>) section.get("series");
                    converted.put("series", serieMapper.toDTOs(series));
                    return converted;
                })
                .toList()
        );
    }

    @GetMapping("/lists")
    public ResponseEntity<List<Map<String, Object>>> getLists(){
        return ResponseEntity.ok(listsService.getAllListSections());
    }

    @GetMapping("/lists/{id}")
    public ResponseEntity<ListsDto> getList(@PathVariable Long id){
        Lists list = listsService.findById(id);
        if(list == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(listsMapper.toDTO(list));
    }

    @PostMapping("/filmographies/{id}/lists/update")
    public ResponseEntity<Void> updateFilmographyLists(@PathVariable Long id, @RequestParam(required = false) List<Long> listIds, Principal principal){
        if(principal == null) return ResponseEntity.status(401).build();
        listsService.updateFilmographyInUserLists(id, listIds, principal.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filmographies/{id}")  // return ok and detail of film
    public ResponseEntity<FilmographyDto> detail(@PathVariable long id) {
        Filmography filmography = filmographyService.findById(id);
        if(filmography == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(filmographyMapper.toDTO(filmography));
    }

    @GetMapping("/filmographies/{id}/reviews")
    public ResponseEntity<List<ReviewDto>> getFilmographyReviews(@PathVariable Long id){
        Filmography filmography = filmographyService.findByIdWithReviews(id);
        if(filmography == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(reviewMapper.toDTOs(filmography.getFilmographyReviews()));
    }
    
    @GetMapping("/films/recent")
    public ResponseEntity<List<MovieDto>> getRecentFilms(){
        List<Movie> movies = filmographyService.getRecentFilms(20);
        return ResponseEntity.ok(movieMapper.toDTOs(movies));
    }

    @GetMapping("/films/genre/{genre}")
    public ResponseEntity<List<MovieDto>> getFilmsByGenre(@PathVariable String genre){
        List<Filmography> films = filmographyService.getFilmsByGenre(genre.toUpperCase());
        List<Movie> movies = films.stream().filter(f -> f instanceof Movie).map(f -> (Movie) f).toList();
        return ResponseEntity.ok(movieMapper.toDTOs(movies));
    }

    @GetMapping("/series/genre/{genre}")
    public ResponseEntity<List<SerieDto>> getSeriesByGenre(@PathVariable String genre){
        List<Serie> series = filmographyService.findSeriesByGenre(es.code.urjc.practica2.model.Genre.Genres.valueOf(genre.toUpperCase()));
        return ResponseEntity.ok(serieMapper.toDTOs(series));
    }
}
