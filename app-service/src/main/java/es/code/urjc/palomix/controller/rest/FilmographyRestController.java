package es.code.urjc.palomix.controller.rest;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.code.urjc.palomix.dto.FilmographyDto;
import es.code.urjc.palomix.dto.HomeResponseDto;
import es.code.urjc.palomix.dto.ListsDto;
import es.code.urjc.palomix.dto.MovieDto;
import es.code.urjc.palomix.dto.ReviewDto;
import es.code.urjc.palomix.dto.SerieDto;
import es.code.urjc.palomix.mapper.FilmographyMapper;
import es.code.urjc.palomix.mapper.ListsMapper;
import es.code.urjc.palomix.mapper.MovieMapper;
import es.code.urjc.palomix.mapper.ReviewMapper;
import es.code.urjc.palomix.mapper.SerieMapper;
import es.code.urjc.palomix.model.Filmography;
import es.code.urjc.palomix.model.Lists;
import es.code.urjc.palomix.model.Movie;
import es.code.urjc.palomix.model.Serie;
import es.code.urjc.palomix.service.FilmographyService;
import es.code.urjc.palomix.service.ListsService;
import es.code.urjc.palomix.service.ReviewService;

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
    public ResponseEntity<Page<ReviewDto>> getFilmographyReviews(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Filmography filmography = filmographyService.findByIdWithReviews(id);
        if (filmography == null) return ResponseEntity.notFound().build();

        List<ReviewDto> allReviews = reviewMapper.toDTOs(filmography.getFilmographyReviews());

        int start = page * size;
        int end = Math.min(start + size, allReviews.size());
        List<ReviewDto> paged = allReviews.subList(start, end);

        Page<ReviewDto> result = new PageImpl<>(paged, PageRequest.of(page, size), allReviews.size());
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/films/recent")
    public ResponseEntity<Page<MovieDto>> getRecentFilms(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size){
        Page<Movie> movies = filmographyService.getRecentFilms(PageRequest.of(page, size));
        return ResponseEntity.ok(movies.map(movieMapper::toDTO));
    }

    @GetMapping("/films/genre/{genre}")
    public ResponseEntity<Page<MovieDto>> getFilmsByGenre(@PathVariable String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<Filmography> films = filmographyService.getFilmsByGenre(genre.toUpperCase());
        List<Movie> movies = films.stream()
            .filter(f -> f instanceof Movie)
            .map(f -> (Movie) f)
            .toList();

        int start = page * size;
        int end = Math.min(start + size, movies.size());
        List<MovieDto> paged = movieMapper.toDTOs(movies.subList(start, end));

        Page<MovieDto> result = new org.springframework.data.domain.PageImpl<>(
            paged, PageRequest.of(page, size), movies.size());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/series/genre/{genre}")
    public ResponseEntity<Page<SerieDto>> getSeriesByGenre(
            @PathVariable String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<Serie> series = filmographyService.findSeriesByGenre(
            es.code.urjc.palomix.model.Genre.Genres.valueOf(genre.toUpperCase()));
        
        // Manual pagination since findSeriesByGenre returns List
        int start = page * size;
        int end = Math.min(start + size, series.size());
        List<SerieDto> paged = serieMapper.toDTOs(series.subList(start, end));
        
        Page<SerieDto> result = new org.springframework.data.domain.PageImpl<>(
            paged, PageRequest.of(page, size), series.size());
        return ResponseEntity.ok(result);
    }
    @GetMapping("/filmographies/{id}/chart")
    public ResponseEntity<Map<Float, Long>> getFilmographyChart(@PathVariable Long id) {
        Filmography filmography = filmographyService.findByIdWithReviews(id);
        if (filmography == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reviewService.getChartDataMap(filmography.getFilmographyReviews()));
    }
}
