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
    String imageUrl,
    String directorName,
    List<Platforms> filmographyPlatforms,
    Integer movieDuration, // null if serie
    Integer serieDuration  //null if movie
    ) {
}

