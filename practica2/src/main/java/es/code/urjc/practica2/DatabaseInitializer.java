package es.code.urjc.practica2;

import java.time.*;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import es.code.urjc.practica2.model.Account;
import es.code.urjc.practica2.model.Account.Role;
import es.code.urjc.practica2.model.Director;
import es.code.urjc.practica2.model.Filmography;
import es.code.urjc.practica2.model.Filmography.Platforms;
import es.code.urjc.practica2.model.Genre;
import es.code.urjc.practica2.model.Genre.Genres;
import es.code.urjc.practica2.model.Lists;
import es.code.urjc.practica2.model.Movie;
import es.code.urjc.practica2.model.Review;
import es.code.urjc.practica2.model.Serie;
import es.code.urjc.practica2.repository.AccountRepository;
import es.code.urjc.practica2.repository.DirectorRepository;
import es.code.urjc.practica2.repository.FilmographyRepository;
import es.code.urjc.practica2.repository.GenreRepository;
import es.code.urjc.practica2.repository.ListsRepository;
import es.code.urjc.practica2.repository.ReviewRepository;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired private AccountRepository accountRepository;
    @Autowired private FilmographyRepository filmographyRepository;
    @Autowired private ReviewRepository reviewRepository;
    @Autowired private ListsRepository listsRepository;
    @Autowired private DirectorRepository directorRepository;
    @Autowired private GenreRepository genreRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // Accounts
        Account admin = new Account("admin", LocalDate.of(1990,1,1), "admin@palomix.com", Role.ADMIN, passwordEncoder.encode("admin"));
        Account alice = new Account("alice",  LocalDate.of(1995,6,15), "alice@palomix.com", Role.USER, passwordEncoder.encode("alice123"));
        Account bob   = new Account("bob", LocalDate.of(1998,11,3), "bob@palomix.com",   Role.USER, passwordEncoder.encode("bob123"));
        accountRepository.saveAll(List.of(admin, alice, bob));

        // Directors
        Director nolan      = new Director("Christopher Nolan", "1970-07-30");
        Director villeneuve = new Director("Denis Villeneuve",  "1967-10-03");
        Director lynch      = new Director("David Lynch",       "1946-01-20");
        Director nolanJ     = new Director("Jonathan Nolan",    "1976-06-06");
        Director gilligan   = new Director("Vince Gilligan",    "1967-02-10");
        directorRepository.saveAll(List.of(nolan, villeneuve, lynch, nolanJ, gilligan));

        // Genres — saved first so they have an ID before being assigned
        Genre accion         = genreRepository.save(new Genre(Genres.ACCION));
        Genre cienciaFiccion = genreRepository.save(new Genre(Genres.CIENCIA_FICCION));
        Genre suspense       = genreRepository.save(new Genre(Genres.SUSPENSE));
        Genre drama          = genreRepository.save(new Genre(Genres.DRAMA));
        Genre miedo          = genreRepository.save(new Genre(Genres.MIEDO));
        Genre aventura       = genreRepository.save(new Genre(Genres.AVENTURA));

        // Movies
        Movie inception = new Movie(
                null,
                "Inception",
                0f,
                "A thief who steals corporate secrets through the use of dream-sharing "
                + "technology is given the inverse task of planting an idea into the mind "
                + "of a C.E.O.",
                2010,
                nolan,
                "https://www.youtube.com/embed/YoHD9XEInc0",
                148
        );
        inception.setFilmographyPlatforms(List.of(Platforms.NETFLIX, Platforms.HBOMAX));
        inception.setFilmographyGenres(List.of(accion, cienciaFiccion, suspense));

        Movie dune = new Movie(
                null,
                "Dune: Part One",
                0f,
                "A noble family becomes embroiled in a war for control over the galaxy's "
                + "most valuable asset while its heir becomes troubled by visions of a "
                + "dark future.",
                2021,
                villeneuve,
                "https://www.youtube.com/embed/8g18jFHCLXk",
                155
        );
        dune.setFilmographyPlatforms(List.of(Platforms.HBOMAX));
        dune.setFilmographyGenres(List.of(cienciaFiccion, aventura, drama));

        Movie mulhollandDrive = new Movie(
                null,
                "Mulholland Drive",
                0f,
                "After a car wreck on the winding Mulholland Drive renders a woman amnesiac, "
                + "she and a perky Hollywood-hopeful search for clues and answers across "
                + "Los Angeles in a twisting venture beyond dreams and reality.",
                2001,
                lynch,
                "https://www.youtube.com/embed/2RKOQUqBxFM",
                147
        );
        mulhollandDrive.setFilmographyPlatforms(List.of(Platforms.PRIMEVIDEO));
        mulhollandDrive.setFilmographyGenres(List.of(suspense, drama, miedo));

        filmographyRepository.saveAll(List.of(inception, dune, mulhollandDrive));

        // Series 
        Serie westworld = new Serie(
                null,
                "Westworld",
                0f,
                "Set at the intersection of the near future and the reimagined past, "
                + "Westworld explores a world in which every human appetite can be "
                + "indulged without consequence.",
                2016,
                nolanJ,
                "https://www.youtube.com/embed/bqMg4crFP5g",
                4
        );
        westworld.setFilmographyPlatforms(List.of(Platforms.HBOMAX));
        westworld.setFilmographyGenres(List.of(cienciaFiccion, drama, suspense));

        Serie breakingBad = new Serie(
                null,
                "Breaking Bad",
                0f,
                "A high school chemistry teacher diagnosed with inoperable lung cancer "
                + "turns to manufacturing and selling methamphetamine in order to secure "
                + "his family's future.",
                2008,
                gilligan,
                "https://www.youtube.com/embed/HhesaQXLuRY",
                5
        );
        breakingBad.setFilmographyPlatforms(List.of(Platforms.NETFLIX));
        breakingBad.setFilmographyGenres(List.of(drama, suspense, accion));

        filmographyRepository.saveAll(List.of(westworld, breakingBad));

        // Reviews
        reviewRepository.saveAll(List.of(
                new Review(5f, "An absolute masterpiece. Nolan at his very best.", alice, inception),
                new Review(4f, "Mind-bending and thrilling. Rewards repeated viewings.", bob, inception),
                new Review(5f, "Visually stunning. A breathtaking adaptation of the novel.", alice, dune),
                new Review(4f, "One of the best sci-fi films in years.", bob, dune),
                new Review(5f, "Television at its finest. Walter White's arc is unmatched.", alice, breakingBad),
                new Review(4f, "Gripping from start to finish.", bob, breakingBad),
                new Review(3f, "Interesting premise but loses steam after season two.", alice, westworld)
        ));

        // Recalculate average stars — reload with reviews so the list is complete
        List.of(
                inception.getFilmographyId(),
                dune.getFilmographyId(),
                mulhollandDrive.getFilmographyId(),
                westworld.getFilmographyId(),
                breakingBad.getFilmographyId()
        ).forEach(id -> {
                Filmography f = filmographyRepository.findByIdWithReviews(id).orElseThrow();
                f.updateAverageStars();
                filmographyRepository.save(f);
        });

        // Lists
        Lists aliceFavourites = new Lists("Favourites", List.of(inception, dune, breakingBad));
        aliceFavourites.setListOwner(alice);

        Lists aliceWatchLater = new Lists("Watch later", List.of(mulhollandDrive, westworld));
        aliceWatchLater.setListOwner(alice);

        Lists bobMustSee = new Lists("Must see", List.of(inception, breakingBad));
        bobMustSee.setListOwner(bob);

        listsRepository.saveAll(List.of(aliceFavourites, aliceWatchLater, bobMustSee));
        inception.setFilmographyImageUrl("https://image.tmdb.org/t/p/w500/qmDpIHrmpJINaRKAfWQfftjCdyi.jpg");
        dune.setFilmographyImageUrl("https://image.tmdb.org/t/p/w500/d5NXSklXo0qyIYkgV94XAgMIckC.jpg");
        mulhollandDrive.setFilmographyImageUrl("https://image.tmdb.org/t/p/w500/qtRK5N2uDGqkV3x3yV2G6e0OQWw.jpg");

        westworld.setFilmographyImageUrl("https://image.tmdb.org/t/p/w500/y55oBgf6bVMI7sFNXwJDrSIxPQt.jpg");
        breakingBad.setFilmographyImageUrl("https://image.tmdb.org/t/p/w500/ggFHVNu6YYI5L9pCfOacjizRGt.jpg");

        filmographyRepository.save(inception);
        filmographyRepository.save(dune);
        filmographyRepository.save(mulhollandDrive);

        filmographyRepository.save(westworld);
        filmographyRepository.save(breakingBad);


    }
}
