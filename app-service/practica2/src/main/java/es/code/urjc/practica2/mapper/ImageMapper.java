package es.code.urjc.practica2.mapper;

import org.mapstruct.Mapper;

import es.code.urjc.practica2.dto.ImageDto;
import es.code.urjc.practica2.model.Image;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    
    ImageDto toDTO(Image image);
}