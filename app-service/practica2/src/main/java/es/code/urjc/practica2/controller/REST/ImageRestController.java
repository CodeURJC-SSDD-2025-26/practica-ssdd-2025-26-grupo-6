package es.code.urjc.practica2.controller.REST;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.code.urjc.practica2.dto.ImageDto;
import es.code.urjc.practica2.model.Image;
import es.code.urjc.practica2.service.ImageService;

@RestController
@RequestMapping("/images")
public class ImageRestController {
    @Autowired
	private ImageService imageService;


	@GetMapping("/{id}")
	public ImageDto getImage(@PathVariable long id) {
        Image img = imageService.getImage(id);
		return new ImageDto(img.getImageId());
	}

	@GetMapping("/{id}/media")
	public ResponseEntity<Object> getImageFile(@PathVariable long id)
			throws SQLException, IOException {

		Resource imageFile = (Resource) imageService.getImageFile(id);

		MediaType mediaType = MediaTypeFactory
				.getMediaType((org.springframework.core.io.Resource) imageFile)
				.orElse(MediaType.IMAGE_JPEG);

		return ResponseEntity
				.ok()
				.contentType(mediaType)
				.body(imageFile);
	}

	@PutMapping("/{id}/media")
	public ResponseEntity<Object> replaceImageFile(@PathVariable long id,
			@RequestParam MultipartFile imageFile) throws IOException {

		imageService.replaceImageFile(id, imageFile.getInputStream());
		return ResponseEntity.noContent().build();
	}
}
