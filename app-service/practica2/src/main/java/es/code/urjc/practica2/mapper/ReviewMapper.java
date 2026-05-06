package es.code.urjc.practica2.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.code.urjc.practica2.dto.ReviewDto;
import es.code.urjc.practica2.model.Review;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "filmographyId",   source = "filmography.filmographyId")
    @Mapping(target = "reviewAuthor", source = "reviewAuthor.accountName")
    ReviewDto toDTO(Review review);

    List<ReviewDto> toDTOs(Collection<Review> reviews);

    @Mapping(target = "filmography", ignore = true)
    @Mapping(target = "reviewAuthor", ignore = true)
    Review toDomain(ReviewDto dto);
}
