package es.code.urjc.practica2.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;

import es.code.urjc.practica2.dto.MovieDto;
import es.code.urjc.practica2.model.Movie;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    MovieDto toDTO(Movie movie);
    List<MovieDto> toDTOs(Collection<Movie> movie);
    Movie toDomain(MovieDto movie);
}
