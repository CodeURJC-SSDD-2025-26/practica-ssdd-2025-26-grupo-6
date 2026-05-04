package es.code.urjc.practica2.dto;

import java.util.List;
import java.util.Map;

//For "/principal"
public record HomeResponseDto(
    List<MovieDto> topMovies,
    List<Map<String, Object>> movieSections) {
}
