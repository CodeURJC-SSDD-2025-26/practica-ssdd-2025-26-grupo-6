package es.code.urjc.practica2.dto;

import java.util.List;

import es.code.urjc.practica2.model.Filmography.Platforms;


public record FilmographyDto(
    Long filmographyId,
    String filmographyName,
    float filmographyAverageStars,
    String filmographySynopsis,
    int filmographyYear,
    String filmographyTrailerUrl,
    ImageDto image,
    String directorName,
    List<Platforms> filmographyPlatforms,
    String type // "MOVIE" o "SERIE"
    ) {
}
