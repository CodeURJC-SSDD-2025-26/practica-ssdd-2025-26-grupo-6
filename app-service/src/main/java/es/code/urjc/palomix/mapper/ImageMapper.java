package es.code.urjc.palomix.mapper;

import org.mapstruct.Mapper;

import es.code.urjc.palomix.dto.ImageDto;
import es.code.urjc.palomix.model.Image;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    
    ImageDto toDTO(Image image);
}