package es.code.urjc.palomix.dto;

import java.util.List;

import es.code.urjc.palomix.model.Genre;
import es.code.urjc.palomix.model.Filmography.Platforms;

public record SerieDto(
                Long id,
                String name,
                float averageStars,
                String synopsis,
                int year,
                String trailerUrl,
                ImageDto image,
                List<Platforms> platforms,
                String directorName,
                int duration,
                List<GenreDto> genres
        ) {
}
