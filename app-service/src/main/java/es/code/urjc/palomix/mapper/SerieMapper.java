package es.code.urjc.palomix.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.code.urjc.palomix.dto.SerieDto;
import es.code.urjc.palomix.model.Movie;
import es.code.urjc.palomix.model.Serie;

@Mapper(componentModel = "spring")
public interface SerieMapper {
    @Mapping(source = "filmographyId", target = "id")
    @Mapping(source = "filmographyName", target = "name")
    @Mapping(source = "filmographyAverageStars", target = "averageStars")
    @Mapping(source = "filmographySynopsis", target = "synopsis")
    @Mapping(source = "filmographyYear", target = "year")
    @Mapping(source = "filmographyTrailerUrl", target = "trailerUrl")
    @Mapping(source = "filmographyImage", target = "image")
    @Mapping(source = "filmographyPlatforms", target = "platforms")
    @Mapping(source = "filmographyDirector.directorName", target = "directorName")
    @Mapping(source = "serieDuration", target = "duration")
    SerieDto toDTO(Serie serie);

    List<SerieDto> toDTOs(Collection<Serie> serie);

    @Mapping(source = "id", target = "filmographyId")
    @Mapping(source = "name", target = "filmographyName")
    @Mapping(source = "averageStars", target = "filmographyAverageStars")
    @Mapping(source = "synopsis", target = "filmographySynopsis")
    @Mapping(source = "year", target = "filmographyYear")
    @Mapping(source = "trailerUrl", target = "filmographyTrailerUrl")
    @Mapping(source = "platforms", target = "filmographyPlatforms")
    @Mapping(source = "duration", target = "serieDuration")
    @Mapping(target = "filmographyImage", ignore = true)
    @Mapping(target = "filmographyDirector", ignore = true)
    @Mapping(target = "filmographyGenres", ignore = true)
    @Mapping(target = "filmographyReviews", ignore = true)
    Serie toDomain(SerieDto serie);

    default String mapImageUrl(Serie movie) {
        if (movie.getFilmographyImage() == null) {
            return null;
        }
        return "/api/images/" + movie.getFilmographyImage().getImageId();
    }
}
