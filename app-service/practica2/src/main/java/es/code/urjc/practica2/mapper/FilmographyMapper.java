package es.code.urjc.practica2.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;

import es.code.urjc.practica2.dto.FilmographyDto;
import es.code.urjc.practica2.model.Filmography;

@Mapper(componentModel = "spring")
public interface FilmographyMapper {
    FilmographyDto toDTO(Filmography filmography);
    List<FilmographyDto> toDTOs(Collection<Filmography> filmography);
    Filmography toDomain(FilmographyDto filmography);
}
