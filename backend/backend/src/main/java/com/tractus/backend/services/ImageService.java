package com.tractus.backend.services;

import com.tractus.backend.models.Image;
import com.tractus.backend.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    public Image uploadImage(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Image image = new Image();
        image.setName(fileName);
        image.setType(file.getContentType());
        image.setData(file.getBytes());
        return imageRepository.save(image);
    }

    public Image getImage(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new RuntimeException("Image not found with id: " + id));
    }
}
