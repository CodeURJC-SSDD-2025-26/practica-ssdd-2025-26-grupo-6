package es.code.urjc.practica2.controller.rest;

import es.code.urjc.practica2.controller.web.AccountController;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.code.urjc.practica2.dto.AccountDto;
import es.code.urjc.practica2.dto.AdminEntityDto;
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
import es.code.urjc.practica2.model.Filmography;
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
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/v1/administrator")
public class AdministratorRestController {

    private final AccountController accountController;
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
    @Autowired
    private DirectorMapper directorMapper;

    AdministratorRestController(AccountController accountController) {
        this.accountController = accountController;
    }

    @GetMapping("/")
    public ResponseEntity<AdminEntityDto> getAllAdminEntity(Pageable pageable) {
        Page<Account> accountsPage = accountService.findAllPage(pageable);
        Page<Movie> moviesPage = filmographyService.findAllMoviesPage(pageable);
        Page<Serie> seriesPage = filmographyService.findAllSeriesPage(pageable);
        Page<Lists> systemListsPage = listsService.findAllSystemListsPage(pageable);
        Page<Director> directorsPage = directorService.findAllPage(pageable);

        AdminEntityDto dashboard = new AdminEntityDto(
                accountsPage.map(accountMapper::toDTO),
                moviesPage.map(movieMapper::toDTO),
                seriesPage.map(serieMapper::toDTO),
                systemListsPage.map(listsMapper::toDTO),
                directorsPage.map(directorMapper::toDTO));
        return ResponseEntity.ok(dashboard);
    }

    // MOVIES
    @PostMapping("/movies")
    public ResponseEntity<?> createMovie(@RequestBody MovieDto dto) {
        Movie movie = movieMapper.toDomain(dto);
        if (filmographyService.getByName(movie.getFilmographyName()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "La película ya existe."));
        }
        var saved = filmographyService.save(movie);
        return ResponseEntity.ok(movieMapper.toDTO((Movie) saved));
    }

    @PutMapping("/movies/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable Long id, @RequestBody MovieDto dto) {
        if (filmographyService.findMovieById(id) == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "La película no existe."));
        Movie movie = movieMapper.toDomain(dto);
        return ResponseEntity.ok(movieMapper.toDTO(filmographyService.updateMovie(id, movie)));
    }

    @DeleteMapping("/movies/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id) {
        if (filmographyService.findMovieById(id) == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "La película no existe."));
        filmographyService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    // SERIES
    @PostMapping("/series")
    public ResponseEntity<?> createSerie(@RequestBody SerieDto dto) {
        Serie serie = serieMapper.toDomain(dto);
        if (filmographyService.getByName(serie.getFilmographyName()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "La serie ya existe."));
        }
        Filmography saved = filmographyService.save(serie);
        return ResponseEntity.status(HttpStatus.CREATED).body(serieMapper.toDTO((Serie) saved));
    }

    @PutMapping("/series/{id}")
    public ResponseEntity<?> updateSerie(@PathVariable Long id, @RequestBody SerieDto dto) {
        if (filmographyService.findById(id) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "La serie no existe."));
        }
        Serie serie = serieMapper.toDomain(dto);
        return ResponseEntity.ok(serieMapper.toDTO(filmographyService.updateSeries(id, serie)));
    }

    @DeleteMapping("/series/{id}")
    public ResponseEntity<?> deleteSerie(@PathVariable Long id) {
        if (filmographyService.findById(id) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "La serie no existe."));
        }
        filmographyService.deleteMovie(id);

        return ResponseEntity.noContent().build();
    }

    // ACCOUNTS
    // update user
    @PutMapping("/accounts/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable Long id, @RequestBody AccountDto accountDto) {
        Account user = accountService.findById(id);
        if (accountService.findById(id) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "La cuenta no existe."));
        }

        user = accountService.updateAccount(user,accountMapper.toDomain(accountDto));

        accountService.save(user);
        return ResponseEntity.ok(accountMapper.toDTO(user));
    }

    // delete user
    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
        if (accountService.findById(id) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "La cuenta no existe."));
        }
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // get user's reviews
    @GetMapping("/accounts/{id}/reviews")
    public ResponseEntity<?> getUserReviews(@PathVariable Long id, Pageable pageable) {
        Account user = accountService.findById(id);
        if (accountService.findById(id) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "La cuenta no existe."));
        }
        ;

        Page<Review> reviewsPage = (user.getAccountRole() == Account.Role.ADMIN)
                ? reviewService.findAllPage(pageable)
                : reviewService.findByAuthorName(user, pageable);

        Page<ReviewDto> dtoPage = reviewsPage.map(reviewMapper::toDTO);
        return ResponseEntity.ok(dtoPage);
    }

    // get user's lists
    @GetMapping("/accounts/{id}/lists")
    public ResponseEntity<?> getUserLists(@PathVariable Long id, Pageable pageable) {
        Account user = accountService.findById(id);
        if (accountService.findById(id) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "La cuenta no existe."));
        }

        Page<Lists> listsPage = (user.getAccountRole() == Account.Role.ADMIN)
                ? listsService.findAllSystemListsPage(pageable)
                : listsService.findAllListsByAuthorPage(user, pageable);

        Page<ListsDto> dtoPage = listsPage.map(listsMapper::toDTO);
        return ResponseEntity.ok(dtoPage);
    }

    // DIRECTOR
    // new Director
    @PostMapping("/directors")
    public ResponseEntity<?> createDirector(@RequestBody DirectorDto directorDto) {
        Director dto = directorMapper.toDomain(directorDto);
        if (directorService.getDirectorByNamePage(dto.getDirectorName()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "El director ya existe."));
        }

        Director director = new Director(directorDto.directorName(),directorDto.directorBirthDate());

        return ResponseEntity.status(HttpStatus.CREATED).body(directorMapper.toDTO( directorService.save(director)));
    }

    // update director
    @PutMapping("/directors/{id}")
    public ResponseEntity<?> updateDirector(@PathVariable Long id, @RequestBody DirectorDto directorDto) {
        Director director = directorService.findById(id);
        if (directorService.findById(id) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "La cuenta no existe."));
        }

        director.setDirectorName(directorDto.directorName());
        director.setDirectorBirthDate(directorDto.directorBirthDate());

        directorService.save(director);
        return ResponseEntity.ok(directorMapper.toDTO(director));
    }

    // delete director and his movies
    @DeleteMapping("/directors/{id}")
    public ResponseEntity<?> deleteDirector(@PathVariable Long id) {
        if (directorService.findById(id) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "La cuenta no existe."));
        }

        directorService.deleteWithFilmographies(id);
        return ResponseEntity.noContent().build();
    }

}
