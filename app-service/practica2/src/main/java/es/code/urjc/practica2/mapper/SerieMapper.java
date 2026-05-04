package es.code.urjc.practica2.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;

import es.code.urjc.practica2.dto.SerieDto;
import es.code.urjc.practica2.model.Serie;

@Mapper(componentModel = "spring")
public interface SerieMapper {
    SerieDto toDTO(Serie serie);

    List<SerieDto> toDTOs(Collection<Serie> serie);

    Serie toDomain(SerieDto serie);
}
