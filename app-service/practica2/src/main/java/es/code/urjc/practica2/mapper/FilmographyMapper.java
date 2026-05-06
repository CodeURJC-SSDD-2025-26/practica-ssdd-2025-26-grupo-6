package es.code.urjc.practica2.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.code.urjc.practica2.dto.FilmographyDto;
import es.code.urjc.practica2.dto.FilmographyBasicDto;
import es.code.urjc.practica2.model.Filmography;

@Mapper(componentModel = "spring", uses = {ImageMapper.class})
public interface FilmographyMapper {
    @Mapping(source = "filmographyImage", target = "image")
    @Mapping(source = "filmographyDirector.directorName", target = "directorName")
    @Mapping(target = "type", ignore = true)
    FilmographyDto toDTO(Filmography filmography);

    @Mapping(source = "filmographyId", target = "id")
    @Mapping(source = "filmographyName", target = "name")
    @Mapping(source = "filmographyAverageStars", target = "averageStars")
    @Mapping(source = "filmographyImage", target = "image")
    @Mapping(target = "type", ignore = true) 
    FilmographyBasicDto toBasicDTO(Filmography filmography);

    List<FilmographyDto> toDTOs(Collection<Filmography> filmography);
    List<FilmographyBasicDto> toBasicDTOs(Collection<Filmography> filmography);

    @Mapping(target = "filmographyImage", ignore = true)
    @Mapping(target = "filmographyDirector", ignore = true)
    @Mapping(target = "filmographyGenres", ignore = true)
    @Mapping(target = "filmographyReviews", ignore = true)
    Filmography toDomain(FilmographyDto filmographyDTO);
}