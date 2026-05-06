package es.code.urjc.practica2.dto;


public record ReviewDto(
        Long reviewId,
        Float reviewStars,
        String reviewDescription,
        Long filmographyId,    
        Long authorId,         
        String authorName
        ) {
}
