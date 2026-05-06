package es.code.urjc.palomix.dto;

import java.util.List;
import java.util.Map;

//For "/principal"
public record HomeResponseDto(
    List<MovieDto> topMovies,
    List<Map<String, Object>> movieSections) {
}
