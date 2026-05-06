package es.code.urjc.practica2.dto;

import java.util.List;

import es.code.urjc.practica2.model.Lists.Types;

public record ListsDto( 
    Long listsId,
    String listName,
    Types type,
    List<FilmographyBasicDto> filmographyList
    ) {
}
