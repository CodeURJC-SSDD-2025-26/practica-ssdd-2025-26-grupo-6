package es.code.urjc.practica2.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;

import es.code.urjc.practica2.dto.ReviewDto;
import es.code.urjc.practica2.model.Review;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewDto toDTO(Review review);
    List<ReviewDto> toDTOs(Collection<Review> review);
    Review toDomain(ReviewDto review);
}
