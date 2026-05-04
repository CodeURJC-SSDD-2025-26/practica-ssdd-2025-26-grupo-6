package es.code.urjc.practica2.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;

import es.code.urjc.practica2.dto.DirectorDto;
import es.code.urjc.practica2.model.Director;

@Mapper(componentModel = "spring")
public interface DirectorMapper {

    DirectorDto toDTO(Director director);
    List<DirectorDto> toDTOs(Collection<Director> director);
    DirectorDto toDomain(DirectorDto director);
}
