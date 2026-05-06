package es.code.urjc.practica2.controller.REST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.code.urjc.practica2.dto.AccountDto;
import es.code.urjc.practica2.dto.DirectorDto;
import es.code.urjc.practica2.dto.ListsDto;
import es.code.urjc.practica2.dto.MovieDto;
import es.code.urjc.practica2.dto.ReviewDto;
import es.code.urjc.practica2.dto.SerieDto;
import es.code.urjc.practica2.mapper.AccountMapper;
import es.code.urjc.practica2.mapper.DirectorMapper;
import es.code.urjc.practica2.mapper.FilmographyMapper;
import es.code.urjc.practica2.mapper.ListsMapper;
import es.code.urjc.practica2.mapper.MovieMapper;
import es.code.urjc.practica2.mapper.ReviewMapper;
import es.code.urjc.practica2.mapper.SerieMapper;
import es.code.urjc.practica2.model.Account;
import es.code.urjc.practica2.model.Director;
import es.code.urjc.practica2.model.Lists;
import es.code.urjc.practica2.model.Movie;
import es.code.urjc.practica2.model.Review;
import es.code.urjc.practica2.model.Serie;
import es.code.urjc.practica2.service.AccountService;
import es.code.urjc.practica2.service.DirectorService;
import es.code.urjc.practica2.service.FilmographyService;
import es.code.urjc.practica2.service.GenreService;
import es.code.urjc.practica2.service.ImageService;
import es.code.urjc.practica2.service.ListsService;
import es.code.urjc.practica2.service.ReviewService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("api/administrator")
public class AdministratorRestController {

    @Autowired
    private FilmographyService filmographyService;
    @Autowired
    private DirectorService directorService;
    @Autowired
    private GenreService genreService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ListsService listsService;
    @Autowired
    private ReviewService reviewService;

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
    @Autowired private DirectorMapper directorMapper;

    // MOVIES
    @PostMapping("/movies")
    public ResponseEntity<MovieDto> createMovie(@RequestBody MovieDto dto) {
        var movie = movieMapper.toDomain(dto);
        var saved = filmographyService.save(movie);
        return ResponseEntity.ok(movieMapper.toDTO((Movie) saved));
    }

    @PutMapping("/movies/{id}")
    public ResponseEntity<MovieDto> updateMovie(@RequestBody MovieDto dto) {
        Movie movie = movieMapper.toDomain(dto);
        if (filmographyService.findMovieById(movie.getFilmographyId()) == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(movieMapper.toDTO(filmographyService.updateMovie(movie.getFilmographyId(), movie)));
    }

    @DeleteMapping("/movies/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        filmographyService.deleteMovie(id);

        return ResponseEntity.noContent().build();
    }

    // SERIES
    @PostMapping("/series")
    public ResponseEntity<MovieDto> createSerie(@RequestBody SerieDto dto) {
        var serie = serieMapper.toDomain(dto);
        var saved = filmographyService.save(serie);
        return ResponseEntity.ok(movieMapper.toDTO((Movie) saved));
    }

    @PutMapping("/series/{id}")
    public ResponseEntity<MovieDto> updateSerie(@RequestBody SerieDto dto) {
        Serie serie = serieMapper.toDomain(dto);
        if (filmographyService.findMovieById(serie.getFilmographyId()) == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(movieMapper.toDTO(filmographyService.updateMovie(serie.getFilmographyId(), serie)));
    }

    @DeleteMapping("/series/{id}")
    public ResponseEntity<Void> deleteSerie(@PathVariable Long id) {
        filmographyService.deleteMovie(id);

        return ResponseEntity.noContent().build();
    }

    // ACCOUNTS
    //update user
    @PutMapping("accounts/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable Long id, @RequestBody AccountDto accountDto) {
        Account user = accountService.findById(id);
        if (user == null)
            return ResponseEntity.notFound().build();

        // Actualizamos los campos permitidos
        user.setAccountName(accountDto.accountName());
        user.setAccountEmail(accountDto.accountEmail());
        user.setAccountBirthDate(accountDto.accountBirthDate());

        accountService.save(user);
        return ResponseEntity.ok(accountMapper.toDTO(user));
    }

    //delete user
    @DeleteMapping("accounts/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        if (accountService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }
    //get user's reviews

    @GetMapping("accounts/{id}/reviews")
    public ResponseEntity<List<ReviewDto>> getUserReviews(@PathVariable Long id) {
        Account user = accountService.findById(id);
        if (user == null)
            return ResponseEntity.notFound().build();

        List<Review> reviews = (user.getAccountRole() == Account.Role.ADMIN)
                ? reviewService.findAll()
                : reviewService.findByAuthor(user);

        return ResponseEntity.ok(reviewMapper.toDTOs(reviews));
    }

    // get user's lists
    @GetMapping("accounts/{id}/lists")
    public ResponseEntity<List<ListsDto>> getUserLists(@PathVariable Long id) {
        Account user = accountService.findById(id);
        if (user == null)
            return ResponseEntity.notFound().build();

        List<Lists> lists = listsService.findAllListsByAuthor(user);
        return ResponseEntity.ok(listsMapper.toDTOs(lists));
    }

    //DIRECTOR
    // new Director
    @PostMapping("directors/")
    public ResponseEntity<DirectorDto> createDirector(@RequestBody DirectorDto directorDto) {
        Director director = new Director();
        director.setDirectorName(directorDto.directorName());
        director.setDirectorBirthDate(directorDto.directorBirthDate());
        
        Director savedDirector = directorService.save(director);
        return ResponseEntity.status(HttpStatus.CREATED).body(directorMapper.toDTO(savedDirector));
    }

    // update director
    @PutMapping("directors/{id}")
    public ResponseEntity<DirectorDto> updateDirector(@PathVariable Long id, @RequestBody DirectorDto directorDto) {
        Director director = directorService.findById(id);
        if (director == null) return ResponseEntity.notFound().build();

        director.setDirectorName(directorDto.directorName());
        director.setDirectorBirthDate(directorDto.directorBirthDate());
        
        directorService.save(director);
        return ResponseEntity.ok(directorMapper.toDTO(director));
    }

    // delete director and his movies
    @DeleteMapping("directors/{id}")
    public ResponseEntity<Void> deleteDirector(@PathVariable Long id) {
        if (directorService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        directorService.deleteWithFilmographies(id);
        return ResponseEntity.noContent().build();
    }

}
