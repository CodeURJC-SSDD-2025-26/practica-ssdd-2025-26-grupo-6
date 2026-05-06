package es.code.urjc.practica2.dto;

import es.code.urjc.practica2.model.Account;
import es.code.urjc.practica2.model.Filmography;

public record ReviewDto(
        Long reviewId,

        Float reviewStars,

        String reviewDescription,

        Long filmographyId,

        String reviewAuthor) {

}
