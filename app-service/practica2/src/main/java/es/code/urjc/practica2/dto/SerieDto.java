package es.code.urjc.practica2.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.code.urjc.practica2.model.Director;
import es.code.urjc.practica2.model.Filmography.Platforms;
import es.code.urjc.practica2.model.Image;

public record SerieDto(
        Long filmographyId,

        String filmographyName,
        float filmographyAverageStars,

        String filmographySynopsis,

        int filmographyYear,
        String filmographyTrailerUrl,

        @JsonIgnore Image filmographyImage,
        String imageUrl,

        List<Platforms> filmographyPlatforms,

        Director filmographyDirector,
        int serieDuration) {

}
