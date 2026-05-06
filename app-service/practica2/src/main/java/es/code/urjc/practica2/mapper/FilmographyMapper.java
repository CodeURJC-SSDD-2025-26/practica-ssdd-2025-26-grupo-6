package es.code.urjc.practica2.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.code.urjc.practica2.dto.FilmographyDto;
import es.code.urjc.practica2.model.Filmography;
import es.code.urjc.practica2.model.Movie;

@Mapper(componentModel = "spring")
public interface FilmographyMapper {

    @Mapping(target = "imageUrl", expression = "java(mapImageUrl(movie))")
    FilmographyDto toDTO(Filmography filmography);

    List<FilmographyDto> toDTOs(Collection<Filmography> filmography);

    @Mapping(target = "filmographyImage", ignore = true)
    Filmography toDomain(FilmographyDto filmography);

    default String mapImageUrl(Movie movie) {
        if (movie.getFilmographyImage() == null) {
            return null;
        }
        return "/api/images/" + movie.getFilmographyImage().getImageId();
    }
}
