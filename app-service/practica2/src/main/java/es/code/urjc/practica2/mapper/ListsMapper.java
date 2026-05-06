package es.code.urjc.practica2.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.code.urjc.practica2.dto.FilmographySummaryDto;
import es.code.urjc.practica2.dto.ListsDto;
import es.code.urjc.practica2.model.Filmography;
import es.code.urjc.practica2.model.Lists;

@Mapper(componentModel = "spring")
public interface ListsMapper {

    @Mapping(target = "listOwner", source = "listOwner.accountName")
    @Mapping(target = "filmographyList", source = "filmographyList")
    ListsDto toDTO(Lists lists);

    List<ListsDto> toDTOs(Collection<Lists> lists);

    @Mapping(target = "listOwner", ignore = true)
    @Mapping(target = "filmographyList", ignore = true)
    Lists toDomain(ListsDto listsDto);

    @Mapping(target = "imageUrl", source = "filmographyImageUrl")
    FilmographySummaryDto toSummary(Filmography filmography);
}
