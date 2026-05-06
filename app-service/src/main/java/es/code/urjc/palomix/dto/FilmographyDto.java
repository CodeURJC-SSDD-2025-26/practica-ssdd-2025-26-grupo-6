package es.code.urjc.palomix.dto;

import java.util.List;

import es.code.urjc.palomix.model.Genre;
import es.code.urjc.palomix.model.Filmography.Platforms;

public record FilmographyDto(
        Long filmographyId,
        String filmographyName,
        float filmographyAverageStars,
        String filmographySynopsis,
        int filmographyYear,
        String filmographyTrailerUrl,
        ImageDto image,
        String directorName,
        List<GenreDto> filmographyGenres,
        List<Platforms> filmographyPlatforms,
        String type // "MOVIE" o "SERIE"
) {
}
