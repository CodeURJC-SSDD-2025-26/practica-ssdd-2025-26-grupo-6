package es.code.urjc.palomix.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.code.urjc.palomix.dto.ListsDto;
import es.code.urjc.palomix.model.Filmography;
import es.code.urjc.palomix.model.Lists;

// Usamos FilmographyMapper para que sepa cómo convertir la lista de películas
@Mapper(componentModel = "spring", uses = {FilmographyMapper.class})
public interface ListsMapper {
    @Mapping(target = "type", ignore = true)
    ListsDto toDTO(Lists lists);

    List<ListsDto> toDTOs(Collection<Lists> lists);

    @Mapping(target = "listOwner", ignore = true) 
    @Mapping(target = "filmographyList", ignore = true)
    Lists toDomain(ListsDto listsDto);
}
