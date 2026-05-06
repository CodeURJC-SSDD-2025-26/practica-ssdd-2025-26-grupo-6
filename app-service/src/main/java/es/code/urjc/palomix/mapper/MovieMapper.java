package es.code.urjc.palomix.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.code.urjc.palomix.dto.MovieDto;
import es.code.urjc.palomix.model.Movie;

@Mapper(componentModel = "spring", uses = { ImageMapper.class })
public interface MovieMapper {

    @Mapping(source = "filmographyId", target = "id")
    @Mapping(source = "filmographyName", target = "name")
    @Mapping(source = "filmographyAverageStars", target = "averageStars")
    @Mapping(source = "filmographySynopsis", target = "synopsis")
    @Mapping(source = "filmographyYear", target = "year")
    @Mapping(source = "filmographyTrailerUrl", target = "trailerUrl")
    @Mapping(source = "filmographyImage", target = "image")
    @Mapping(source = "filmographyPlatforms", target = "platforms")
    @Mapping(source = "filmographyDirector.directorName", target = "directorName")
    @Mapping(source = "movieDuration", target = "duration")
    @Mapping(target = "genres", source = "filmographyGenres")
    MovieDto toDTO(Movie movie);

    List<MovieDto> toDTOs(Collection<Movie> movies);

    @Mapping(source = "id", target = "filmographyId")
    @Mapping(source = "name", target = "filmographyName")
    @Mapping(source = "averageStars", target = "filmographyAverageStars")
    @Mapping(source = "synopsis", target = "filmographySynopsis")
    @Mapping(source = "year", target = "filmographyYear")
    @Mapping(source = "trailerUrl", target = "filmographyTrailerUrl")
    @Mapping(source = "platforms", target = "filmographyPlatforms")
    @Mapping(source = "duration", target = "movieDuration")
    @Mapping(target = "filmographyImage", ignore = true)
    @Mapping(target = "filmographyDirector", ignore = true)
    @Mapping(target = "filmographyGenres", ignore = true)
    @Mapping(target = "filmographyReviews", ignore = true)
    Movie toDomain(MovieDto movieDto);
}
