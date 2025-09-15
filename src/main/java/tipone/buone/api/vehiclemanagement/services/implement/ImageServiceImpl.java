package tipone.buone.api.vehiclemanagement.services.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tipone.buone.api.vehiclemanagement.enums.ErrorCode;
import tipone.buone.api.vehiclemanagement.exceptions.AppException;
import tipone.buone.api.vehiclemanagement.services.CloudinaryService;
import tipone.buone.api.vehiclemanagement.services.ImageService;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final CloudinaryService cloudinaryService;

    @Override
    public String imagePath(MultipartFile multipartFile){
        if (multipartFile != null){
            if (isValidSuffixImage(multipartFile)){
                try {
                    return cloudinaryService.uploadFile(multipartFile);
                } catch (IOException e) {
                    throw new AppException(ErrorCode.UPLOAD_FAILED);
                }
            }
            throw new AppException(ErrorCode.INVALID_IMAGE_FORMAT);
        }
        return "";
    }

    @Override
    public boolean isValidSuffixImage(MultipartFile file) {
        String imagePath = file.getOriginalFilename();
        if (imagePath == null)
            return false;
        return imagePath.endsWith(".png")||imagePath.endsWith(".jpeg")||imagePath.endsWith(".jpg");
    }

    @Override
    public boolean deleteImage(String imageId) {
        if (imageId == null || imageId.trim().isEmpty()) {
            return true;
        }
        try {
            cloudinaryService.deleteFile(imageId);
            return true;
        } catch (IOException e) {
            throw new AppException(ErrorCode.IMAGE_DELETE_FAILED);
        }
    }
}