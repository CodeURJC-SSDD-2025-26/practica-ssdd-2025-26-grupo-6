package es.code.urjc.practica2.dto;

import org.springframework.data.domain.Page;

public record AdminEntityDto(
    Page<AccountDto> accounts,
    Page<MovieDto> movies,
    Page<SerieDto> series,
    Page<ListsDto> systemLists,
    Page<DirectorDto> directors
) {

}
