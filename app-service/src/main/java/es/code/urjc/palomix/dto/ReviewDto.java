package es.code.urjc.palomix.dto;


public record ReviewDto(
        Long reviewId,
        Float reviewStars,
        String reviewDescription,
        Long filmographyId,    
        Long authorId,         
        String authorName
        ) {
}
