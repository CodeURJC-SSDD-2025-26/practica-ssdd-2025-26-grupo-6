package es.code.urjc.practica2.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.code.urjc.practica2.dto.MovieDto;
import es.code.urjc.practica2.model.Movie;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    @Mapping(target = "imageUrl", expression = "java(mapImageUrl(movie))")
    MovieDto toDTO(Movie movie);

    List<MovieDto> toDTOs(Collection<Movie> movies);

    @Mapping(target = "filmographyImage", ignore = true) 
    Movie toDomain(MovieDto movie);

    default String mapImageUrl(Movie movie) {
        if (movie.getFilmographyImage() == null) {
            return null;
        }
        return "/api/images/" + movie.getFilmographyImage().getImageId();
    }
}
