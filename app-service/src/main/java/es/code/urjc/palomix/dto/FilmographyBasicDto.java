package es.code.urjc.palomix.dto;

public record FilmographyBasicDto (
    Long id,
    String name,
    float averageStars,
    String type, // "MOVIE" o "SERIE"
    ImageDto image 
    ){
}
