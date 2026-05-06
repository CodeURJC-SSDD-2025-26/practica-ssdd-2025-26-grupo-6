package es.code.urjc.palomix.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Objects;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import es.code.urjc.palomix.model.Image;
import es.code.urjc.palomix.repository.ImageRepository;

@Service
public class ImageService {
    @Autowired private ImageRepository imageRepository;

    public Image getImage(long id) {
        return imageRepository.findById(id).orElseThrow();
    }

    public Image findById(Long id){
        return imageRepository.findById(Objects.requireNonNull(id)).orElseThrow();
    }

    public List<Image> getAvatarOptions(){
        return imageRepository.findAllById(Objects.requireNonNull(List.of(1L,2L,3L,4L)));
    }

    public Image createImage(InputStream inputStream) throws IOException {

        Image image = new Image();

        try {
            image.setImageFile(new SerialBlob(inputStream.readAllBytes()));
        } catch (Exception e) {
            throw new IOException("Failed to create image", e);
        }

        imageRepository.save(image);

        return image;
    }

    public Resource getImageFile(long id) throws SQLException {

        Image image = imageRepository.findById(id).orElseThrow();

        if (image.getImageFile() != null) {
            return new InputStreamResource(Objects.requireNonNull(image.getImageFile().getBinaryStream()));
        } else {
            throw new RuntimeException("Image file not found");
        }
    }

    public Image replaceImageFile(long id, InputStream inputStream) throws IOException {

        Image image = imageRepository.findById(id).orElseThrow();

        try {
            image.setImageFile(new SerialBlob(inputStream.readAllBytes()));
        } catch (Exception e) {
            throw new IOException("Failed to create image", e);
        }

        imageRepository.save(image);
        
        return image;
    }

    public Image deleteImage(long id) {
        Image image = imageRepository.findById(id).orElseThrow();
        imageRepository.deleteById(id);
        return image;
    }
}