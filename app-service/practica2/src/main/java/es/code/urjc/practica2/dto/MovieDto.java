package es.code.urjc.practica2.dto;

import java.util.List;

import es.code.urjc.practica2.model.Filmography.Platforms;

public record MovieDto(
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
