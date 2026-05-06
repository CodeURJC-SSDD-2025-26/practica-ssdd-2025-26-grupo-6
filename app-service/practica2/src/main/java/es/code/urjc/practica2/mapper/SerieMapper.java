package es.code.urjc.practica2.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.code.urjc.practica2.dto.SerieDto;
import es.code.urjc.practica2.model.Movie;
import es.code.urjc.practica2.model.Serie;

@Mapper(componentModel = "spring")
public interface SerieMapper {
    @Mapping(target = "imageUrl", expression = "java(mapImageUrl(movie))")
    SerieDto toDTO(Serie serie);

    List<SerieDto> toDTOs(Collection<Serie> serie);

    @Mapping(target = "filmographyImage", ignore = true)
    Serie toDomain(SerieDto serie);

    default String mapImageUrl(Serie movie) {
        if (movie.getFilmographyImage() == null) {
            return null;
        }
        return "/api/images/" + movie.getFilmographyImage().getImageId();
    }
}
