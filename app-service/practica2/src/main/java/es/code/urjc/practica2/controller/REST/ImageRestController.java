package es.code.urjc.practica2.controller.rest;

import java.io.IOException;
import java.sql.Blob;
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
@RequestMapping("/api/v1/img")
public class ImageRestController {
	@Autowired
	private ImageService imageService;

	@GetMapping("/{id}")
	public ResponseEntity<byte[]> getImage(@PathVariable Long id) throws Exception {
		Image img = imageService.findById(id);

		Blob blob = img.getImageFile();
		byte[] bytes = blob.getBytes(1, (int) blob.length());

		return ResponseEntity.ok()
				.header("Content-Type", "image/jpg") 
				.body(bytes);
	}
}
