package es.code.urjc.palomix.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;

import es.code.urjc.palomix.dto.DirectorDto;
import es.code.urjc.palomix.model.Director;

@Mapper(componentModel = "spring")
public interface DirectorMapper {

    DirectorDto toDTO(Director director);
    List<DirectorDto> toDTOs(Collection<Director> director);
    Director toDomain(DirectorDto director);
}
