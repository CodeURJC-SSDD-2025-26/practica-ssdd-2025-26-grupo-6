package es.code.urjc.practica2.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;

import es.code.urjc.practica2.dto.ListsDto;
import es.code.urjc.practica2.model.Lists;

@Mapper(componentModel = "spring")
public interface ListsMapper {
    ListsDto toDTO(Lists lists);

    List<ListsDto> toDTOs(Collection<Lists> lists);

    Lists toDomain(ListsDto listsDto);
}
