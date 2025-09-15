package tipone.buone.api.vehiclemanagement.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String imagePath(MultipartFile multipartFile);
    boolean isValidSuffixImage(MultipartFile file);
    boolean deleteImage(String image);
}