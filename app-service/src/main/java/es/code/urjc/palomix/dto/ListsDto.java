package es.code.urjc.palomix.dto;

import java.util.List;

import es.code.urjc.palomix.model.Lists.Types;

public record ListsDto( 
    Long listsId,
    String listName,
    Types type,
    List<FilmographyBasicDto> filmographyList
    ) {
}
