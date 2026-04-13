package es.code.urjc.practica2.service;

import java.io.IOException;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import es.code.urjc.practica2.model.Account;
import es.code.urjc.practica2.model.Account.Role;
import es.code.urjc.practica2.model.Director;
import es.code.urjc.practica2.model.Filmography;
import es.code.urjc.practica2.model.Filmography.Platforms;
import es.code.urjc.practica2.model.Genre;
import es.code.urjc.practica2.model.Genre.Genres;
import es.code.urjc.practica2.model.Image;
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
    @Autowired private ImageService imageService;
    @Autowired private FilmographyService filmographyService;

    @Override
    public void run(String... args) {
        // Accounts
        Account admin = new Account("admin", LocalDate.of(1990,1,1), "admin@palomix.com", Role.ADMIN, passwordEncoder.encode("admin"));
        Account alice = new Account("alice",  LocalDate.of(1995,6,15), "alice@palomix.com", Role.USER, passwordEncoder.encode("alice123"));
        Account bob   = new Account("bob", LocalDate.of(1998,11,3), "bob@palomix.com",   Role.USER, passwordEncoder.encode("bob123"));
        Account carla   = new Account("carla", LocalDate.of(2005,07,15), "carlagrsmm@gmail.com",   Role.USER, passwordEncoder.encode("carla"));
        Account charlie = new Account("charlie", LocalDate.of(1992, 3, 20), "charlie@palomix.com", Role.USER, passwordEncoder.encode("charlie123"));
        Account diana   = new Account("diana",   LocalDate.of(1994, 8, 12), "diana@palomix.com",   Role.USER, passwordEncoder.encode("diana123"));
        Account eve     = new Account("eve",     LocalDate.of(2000, 1, 30), "eve@palomix.com",     Role.ADMIN, passwordEncoder.encode("eve123"));
        Account frank   = new Account("frank",   LocalDate.of(1985, 12, 5), "frank@palomix.com",   Role.USER, passwordEncoder.encode("frank123"));
        Account grace   = new Account("grace",   LocalDate.of(1997, 5, 22), "grace@palomix.com",   Role.USER, passwordEncoder.encode("grace123"));
        Account hector  = new Account("hector",  LocalDate.of(1991, 7, 14), "hector@palomix.com",  Role.USER, passwordEncoder.encode("hector123"));
        Account ivan    = new Account("ivan",    LocalDate.of(1999, 2, 28), "ivan@palomix.com",    Role.USER, passwordEncoder.encode("ivan123"));
        Account julia   = new Account("julia",   LocalDate.of(1993, 10, 10), "julia@palomix.com",  Role.USER, passwordEncoder.encode("julia123"));
        Account kevin   = new Account("kevin",   LocalDate.of(1988, 4, 18), "kevin@palomix.com",   Role.USER, passwordEncoder.encode("kevin123"));
        Account laura   = new Account("laura",   LocalDate.of(1996, 12, 25), "laura@palomix.com",  Role.USER, passwordEncoder.encode("laura123"));

        accountRepository.saveAll(Objects.requireNonNull(List.of(admin, alice, bob, carla, charlie, diana, eve, frank, grace, hector, ivan, julia, kevin, laura)));

        // Directors
        Director nolan      = new Director("Christopher Nolan", "1970-07-30");
        Director villeneuve = new Director("Denis Villeneuve",  "1967-10-03");
        Director lynch      = new Director("David Lynch",       "1946-01-20");
        Director nolanJ     = new Director("Jonathan Nolan",    "1976-06-06");
        Director gilligan   = new Director("Vince Gilligan",    "1967-02-10");
        Director scorsese   = new Director("Martin Scorsese",    "1942-11-17");
        Director tarantino  = new Director("Quentin Tarantino",  "1963-03-27");
        Director spielberg  = new Director("Steven Spielberg",   "1946-12-18");
        Director hitchcock  = new Director("Alfred Hitchcock",   "1899-08-13");
        Director kubrick    = new Director("Stanley Kubrick",     "1928-07-26");
        Director greta      = new Director("Greta Gerwig",       "1983-08-04");
        Director bong       = new Director("Bong Joon-ho",       "1969-09-14");
        Director armstrong  = new Director("Jesse Armstrong",    "1970-12-13"); 
        Director simon      = new Director("David Simon",        "1960-02-09"); 
        Director hwang      = new Director("Hwang Dong-hyuk",    "1971-12-15"); 

        directorRepository.saveAll(Objects.requireNonNull(List.of(nolan, villeneuve, lynch, nolanJ, gilligan, scorsese, tarantino, spielberg, hitchcock, kubrick, greta, bong, armstrong, simon, hwang)));

        // Genres — saved first so they have an ID before being assigned
        Genre accion         = genreRepository.save(new Genre(Genres.ACCION));
        Genre romance        = genreRepository.save(new Genre(Genres.ROMANCE));
        Genre cienciaFiccion = genreRepository.save(new Genre(Genres.CIENCIA_FICCION));
        Genre suspense       = genreRepository.save(new Genre(Genres.SUSPENSE));
        Genre drama          = genreRepository.save(new Genre(Genres.DRAMA));
        Genre miedo          = genreRepository.save(new Genre(Genres.MIEDO));
        Genre aventura       = genreRepository.save(new Genre(Genres.AVENTURA));
        Genre comedia        = genreRepository.save(new Genre(Genres.COMEDIA));
        Genre crimen         = genreRepository.save(new Genre(Genres.CRIMEN));

        // Movies
        Movie inception = new Movie(
                null, "Inception", 0f,
                "Un ladrón que roba secretos corporativos a través del uso de la tecnología para "
                + "compartir sueños recibe la tarea inversa de plantar una idea en la mente "
                + "de un director ejecutivo.",
                2010, nolan, "https://www.youtube.com/embed/YoHD9XEInc0", 148
        );
        filmographyRepository.save(inception);
        inception.setFilmographyPlatforms(List.of(Platforms.NETFLIX, Platforms.HBOMAX));
        inception.setFilmographyGenres(List.of(accion, cienciaFiccion, suspense));
        try { setFilmographyImage(inception,"posters/inception.jpg"); } catch (IOException e) { e.printStackTrace(); }

        Movie dune = new Movie(
                null, "Dune: Parte Uno", 0f,
                "Una familia noble se ve envuelta en una guerra por el control del activo más "
                + "valioso de la galaxia, mientras su heredero se ve perturbado por visiones "
                + "de un futuro oscuro.",
                2021, villeneuve, "https://www.youtube.com/embed/8g18jFHCLXk", 155
        );
        filmographyRepository.save(dune);
        dune.setFilmographyPlatforms(List.of(Platforms.HBOMAX));
        dune.setFilmographyGenres(List.of(cienciaFiccion, aventura, drama));
        try { setFilmographyImage(dune,"posters/dune.jpg"); } catch (IOException e) { e.printStackTrace(); }

        Movie mulhollandDrive = new Movie(
                null, "Mulholland Drive", 0f,
                "Tras un accidente de coche en la sinuosa carretera Mulholland Drive que deja a "
                + "una mujer amnésica, ella y una aspirante a actriz buscan respuestas en una "
                + "aventura laberíntica entre los sueños y la realidad.",
                2001, lynch, "https://www.youtube.com/embed/2RKOQUqBxFM", 147
        );
        filmographyRepository.save(mulhollandDrive);
        mulhollandDrive.setFilmographyPlatforms(List.of(Platforms.PRIMEVIDEO));
        mulhollandDrive.setFilmographyGenres(List.of(suspense, drama, miedo));
        try { setFilmographyImage(mulhollandDrive,"posters/mulholland.jpg"); } catch (IOException e) { e.printStackTrace(); }

        Movie pulpFiction = new Movie(
                null, "Pulp Fiction", 0f,
                "Las vidas de dos sicarios de la mafia, un boxeador, la esposa de un gánster "
                + "y una pareja de bandidos se entrelazan en cuatro historias de violencia "
                + "y redención.",
                1994, tarantino, "https://www.youtube.com/embed/s7EdQ4FqbhY", 154
        );
        filmographyRepository.save(pulpFiction);
        pulpFiction.setFilmographyPlatforms(List.of(Platforms.NETFLIX));
        pulpFiction.setFilmographyGenres(List.of(crimen, drama));
        try { setFilmographyImage(pulpFiction,"posters/pulpfiction.jpg"); } catch (IOException e) { e.printStackTrace(); }

        Movie interstellar = new Movie(
                null, "Interstellar", 0f,
                "Un equipo de exploradores viaja a través de un agujero de gusano en el espacio "
                + "en un intento por asegurar la supervivencia de la humanidad ante una Tierra "
                + "que se muere.",
                2014, nolan, "https://www.youtube.com/embed/zSWdZVtXT7E", 169
        );
        filmographyRepository.save(interstellar);
        interstellar.setFilmographyPlatforms(List.of(Platforms.HBOMAX, Platforms.PRIMEVIDEO));
        interstellar.setFilmographyGenres(List.of(cienciaFiccion, drama, aventura));
        try { setFilmographyImage(interstellar,"posters/interstellar.jpg"); } catch (IOException e) { e.printStackTrace(); }

        Movie parasitos = new Movie(
                null, "Parásitos", 0f,
                "La codicia y la discriminación de clase amenazan la relación recién formada "
                + "entre la adinerada familia Park y el clan Kim, que vive en la precariedad.",
                2019, bong, "https://www.youtube.com/embed/m4ncWL7FvY0", 132
        );
        filmographyRepository.save(parasitos);
        parasitos.setFilmographyPlatforms(List.of(Platforms.NETFLIX));
        parasitos.setFilmographyGenres(List.of(drama, suspense));
        try { setFilmographyImage(parasitos,"posters/parasitos.jpg"); } catch (IOException e) { e.printStackTrace(); }

        Movie odiseaEspacio = new Movie(
                null, "2001: Una odisea del espacio", 0f,
                "Tras descubrir un misterioso artefacto enterrado bajo la superficie lunar, "
                + "la humanidad envía una nave a Júpiter para buscar sus orígenes con la ayuda "
                + "de la supercomputadora HAL 9000.",
                1968, kubrick, "https://www.youtube.com/embed/oR_e9y-bka0", 149
        );
        filmographyRepository.save(odiseaEspacio);
        odiseaEspacio.setFilmographyPlatforms(List.of(Platforms.HBOMAX));
        odiseaEspacio.setFilmographyGenres(List.of(cienciaFiccion, aventura));
        try { setFilmographyImage(odiseaEspacio,"posters/2001.jpg"); } catch (IOException e) { e.printStackTrace(); }

        // Series 
        Serie westworld = new Serie(
                null, "Westworld", 0f,
                "Situada en la intersección del futuro cercano y el pasado reimaginado, "
                + "Westworld explora un mundo en el que cada apetito humano puede ser "
                + "satisfecho sin consecuencias.",
                2016, nolanJ, "https://www.youtube.com/embed/bqMg4crFP5g", 4
        );
        filmographyRepository.save(westworld);
        westworld.setFilmographyPlatforms(List.of(Platforms.HBOMAX));
        westworld.setFilmographyGenres(List.of(cienciaFiccion, drama, suspense));
        try { setFilmographyImage(westworld,"posters/westworld.jpg"); } catch (IOException e) { e.printStackTrace(); }

        Serie breakingBad = new Serie(
                null, "Breaking Bad", 0f,
                "Un profesor de química de secundaria diagnosticado con cáncer de pulmón "
                + "inoperable recurre a la fabricación y venta de metanfetamina para asegurar "
                + "el futuro de su familia.",
                2008, gilligan, "https://www.youtube.com/embed/HhesaQXLuRY", 5
        );
        filmographyRepository.save(breakingBad);
        breakingBad.setFilmographyPlatforms(List.of(Platforms.NETFLIX));
        breakingBad.setFilmographyGenres(List.of(drama, suspense, accion, comedia));
        try { setFilmographyImage(breakingBad,"posters/breakingBad.jpg"); } catch (IOException e) { e.printStackTrace(); }

        Serie succession = new Serie(
                null, "Succession", 0f,
                "La saga de una familia disfuncional dueña de un imperio de medios de comunicación "
                + "que lucha por el control de la empresa ante la incertidumbre sobre la salud "
                + "del patriarca Logan Roy.",
                2018, armstrong, "https://www.youtube.com/embed/OzYxJV_9TnM", 4
        );
        filmographyRepository.save(succession);
        succession.setFilmographyPlatforms(List.of(Platforms.HBOMAX));
        succession.setFilmographyGenres(List.of(drama, comedia));
        try { setFilmographyImage(succession,"posters/succession.jpg"); } catch (IOException e) { e.printStackTrace(); }

        Serie theWire = new Serie(
                null, "The Wire (Bajo escucha)", 0f,
                "Una mirada realista al mundo del narcotráfico en Baltimore, vista a través de "
                + "los ojos de los traficantes y de las fuerzas del orden que intentan detenerlos.",
                2002, simon, "https://www.youtube.com/embed/9qK-VGjMr8g", 5
        );
        filmographyRepository.save(theWire);
        theWire.setFilmographyPlatforms(List.of(Platforms.HBOMAX));
        theWire.setFilmographyGenres(List.of(drama, crimen, suspense));
        try { setFilmographyImage(theWire,"posters/thewire.jpg"); } catch (IOException e) { e.printStackTrace(); }

        Serie squidGame = new Serie(
                null, "El Juego del Calamar", 0f,
                "Cientos de jugadores cortos de dinero aceptan una extraña invitación para competir "
                + "en juegos infantiles. Dentro les espera un premio tentador con apuestas mortales.",
                2021, hwang, "https://www.youtube.com/embed/oqxAJKy0ii4", 1
        );
        filmographyRepository.save(squidGame);
        squidGame.setFilmographyPlatforms(List.of(Platforms.NETFLIX));
        squidGame.setFilmographyGenres(List.of(accion, drama, suspense));
        try { setFilmographyImage(squidGame,"posters/squidgame.jpg"); } catch (IOException e) { e.printStackTrace(); }

        Serie betterCallSaul = new Serie(
                null, "Better Call Saul", 0f,
                "La metamorfosis del abogado de poca monta Jimmy McGill en el carismático y poco "
                + "ético Saul Goodman antes de los eventos de Breaking Bad.",
                2015, gilligan, "https://www.youtube.com/embed/HN4oydykJFc", 6
        );
        filmographyRepository.save(betterCallSaul);
        betterCallSaul.setFilmographyPlatforms(List.of(Platforms.NETFLIX));
        betterCallSaul.setFilmographyGenres(List.of(drama, crimen));
        try { setFilmographyImage(betterCallSaul,"posters/bettercallsaul.jpg"); } catch (IOException e) { e.printStackTrace(); }

        // Reviews
        reviewRepository.saveAll(Objects.requireNonNull(List.of(
                // Origen (Inception)
                new Review(5f, "Una obra maestra absoluta. Nolan en su mejor momento.", alice, inception),
                new Review(4f, "Mente asombrosa y emocionante. Gana con cada visionado.", bob, inception),
                
                // Dune
                new Review(5f, "Visualmente impresionante. Una adaptación del libro que quita el aliento.", alice, dune),
                new Review(4f, "Una de las mejores películas de ciencia ficción en años.", bob, dune),
                
                // Breaking Bad
                new Review(5f, "Televisión en su máxima expresión. El arco de Walter White no tiene rival.", alice, breakingBad),
                new Review(2f, "Adictiva de principio a fin.", bob, breakingBad),
                
                // Westworld
                new Review(3f, "Premisa interesante pero pierde fuelle después de la segunda temporada.", alice, westworld),
                
                // Pulp Fiction
                new Review(5f, "Diálogos brillantes y una estructura narrativa revolucionaria.", charlie, pulpFiction),
                new Review(5f, "Tarantino redefinió el cine de los 90 con esta película.", diana, pulpFiction),

                // Interstellar
                new Review(5f, "Una odisea emocional y científica. La banda sonora de Hans Zimmer es increíble.", eve, interstellar),
                new Review(4.5f, "Te deja pensando en el tiempo y el amor mucho después de los créditos.", frank, interstellar),

                // Parásitos
                new Review(5f, "Una crítica social punzante mezclada con un suspense magistral.", grace, parasitos),
                new Review(5f, "Impresionante cómo cambia de género a mitad de la película. Cine puro.", hector, parasitos),

                // Succession
                new Review(5f, "Shakespeare en la era de los medios modernos. Los diálogos son puñales.", ivan, succession),
                new Review(4.5f, "Odiar a gente rica nunca había sido tan entretenido.", julia, succession),

                // El Juego del Calamar
                new Review(4f, "Un thriller sangriento y adictivo con un trasfondo social muy duro.", kevin, squidGame),
                new Review(4f, "No puedes dejar de mirar aunque sea una pesadilla.", laura, squidGame),

                // Better Call Saul
                new Review(5f, "Rara vez una precuela está a la altura de la original, pero esta a veces la supera.", charlie, betterCallSaul),
                new Review(4.5f, "La evolución de Jimmy a Saul es lenta, pero magistralmente ejecutada.", diana, betterCallSaul)
        )));

        // Recalculate average stars — reload with reviews so the list is complete
        filmographyService.recalculateAllAverages();

        // Lists
        Lists aliceFavourites = new Lists("Favourites", List.of(inception, dune, breakingBad));
        aliceFavourites.setListOwner(alice);

        Lists aliceWatchLater = new Lists("Watch later", List.of(mulhollandDrive, westworld));
        aliceWatchLater.setListOwner(alice);

        Lists bobMustSee = new Lists("Must see", List.of(inception, breakingBad));
        bobMustSee.setListOwner(bob);

        listsRepository.saveAll(Objects.requireNonNull(List.of(aliceFavourites, aliceWatchLater, bobMustSee)));

        // Systems lists
        Lists actionList = new Lists("Acción", new ArrayList<>(List.of(inception, breakingBad, pulpFiction, squidGame)));
        actionList.setListOwner(admin);

        Lists adventureList = new Lists("Aventura", new ArrayList<>(List.of(dune, interstellar, odiseaEspacio)));
        adventureList.setListOwner(admin);

        Lists sciFiList = new Lists("Ciencia Ficción", new ArrayList<>(List.of(inception, dune, westworld, interstellar, odiseaEspacio)));
        sciFiList.setListOwner(admin);

        Lists dramaList = new Lists("Drama", new ArrayList<>(List.of(dune, breakingBad, westworld, mulhollandDrive, pulpFiction, parasitos, succession, theWire, interstellar, betterCallSaul)));
        dramaList.setListOwner(admin);

        Lists suspenseList = new Lists("Suspense", new ArrayList<>(List.of(inception, mulhollandDrive, westworld, breakingBad, parasitos, theWire, squidGame)));
        suspenseList.setListOwner(admin);

        Lists horrorList = new Lists("Miedo", new ArrayList<>(List.of(mulhollandDrive)));
        horrorList.setListOwner(admin);

        Lists comedyList = new Lists("Comedia", new ArrayList<>(List.of(breakingBad, succession)));
        comedyList.setListOwner(admin);

        Lists romanceList = new Lists("Romance", new ArrayList<>(List.of()));
        romanceList.setListOwner(admin);

        // Nueva lista de Crimen para aprovechar los nuevos directores
        Lists crimeList = new Lists("Crimen", new ArrayList<>(List.of(pulpFiction, theWire, betterCallSaul)));
        crimeList.setListOwner(admin);

        listsRepository.saveAll(Objects.requireNonNull(List.of(actionList, adventureList, sciFiList, dramaList, suspenseList, horrorList, comedyList, romanceList, crimeList)));

        // Listas de sistema específicas para Series (Propiedad del administrador)
        Lists sciFiSeriesList = new Lists("Ciencia Ficción - Series", new ArrayList<>(List.of(westworld)));
        sciFiSeriesList.setListOwner(admin);

        Lists dramaSeriesList = new Lists("Drama - Series", new ArrayList<>(List.of(westworld, breakingBad, succession, theWire, betterCallSaul, squidGame)));
        dramaSeriesList.setListOwner(admin);

        Lists suspenseSeriesList = new Lists("Suspense - Series", new ArrayList<>(List.of(westworld, breakingBad, theWire, squidGame)));
        suspenseSeriesList.setListOwner(admin);

        Lists actionSeriesList = new Lists("Acción - Series", new ArrayList<>(List.of(breakingBad, squidGame)));
        actionSeriesList.setListOwner(admin);

        Lists comedySeriesList = new Lists("Comedia - Series", new ArrayList<>(List.of(breakingBad, succession)));
        comedySeriesList.setListOwner(admin);

        listsRepository.saveAll(Objects.requireNonNull(List.of(sciFiSeriesList, dramaSeriesList, suspenseSeriesList, actionSeriesList, comedySeriesList)));
    }

    public void setFilmographyImage(Filmography film, String classPathResource) throws IOException{
        if (classPathResource == null) {
            throw new IllegalArgumentException("classPathResource cannot be null");
        }

        Resource image = new ClassPathResource(classPathResource);

        Image createdImage = imageService.createImage(image.getInputStream());
        film.setFilmographyImage(createdImage);

        filmographyRepository.save(film);
    }
}
