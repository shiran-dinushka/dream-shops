package com.dailywork.dreamshops.service.image;

import com.dailywork.dreamshops.dto.ImageDto;
import com.dailywork.dreamshops.exceptions.ResourceNotFoundException;
import com.dailywork.dreamshops.model.Image;
import com.dailywork.dreamshops.model.Product;
import com.dailywork.dreamshops.repository.CategoryRepository;
import com.dailywork.dreamshops.repository.ImageRepository;
import com.dailywork.dreamshops.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{

    private final ImageRepository imageRepository;
    private final IProductService productService;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No image found with id " + id));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete,
                ()->{throw new ResourceNotFoundException("No image found with id " + id);
        });
    }

    @Override
    public List<ImageDto> saveImage(List<MultipartFile> file, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> savedImagesDto = new ArrayList<>() ;
        for (MultipartFile multipartFile : file) {
            try {
                Image image = new Image();
                image.setFilename(multipartFile.getOriginalFilename());
                image.setFileType(multipartFile.getContentType());
                image.setImage(new SerialBlob(multipartFile.getBytes()));
                image.setProduct(product);

                String buildDownloadUrl = "/api/v1/images/image/download/";
                String downloadUrl = buildDownloadUrl + image.getId();
                image.setDownloadUrl(downloadUrl);
                Image saveImage = imageRepository.save(image);

                ImageDto imageDto = new ImageDto();
                imageDto.setImageId(saveImage.getId());
                imageDto.setImageName(saveImage.getFilename());
                imageDto.setDownloadUrl(saveImage.getDownloadUrl());
                savedImagesDto .add(imageDto);

            }catch (IOException | SQLException e){
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImagesDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        try {
            image.setFilename(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
