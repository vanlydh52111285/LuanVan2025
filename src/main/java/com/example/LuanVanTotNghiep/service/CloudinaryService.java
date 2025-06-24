package com.example.LuanVanTotNghiep.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudinaryService {
    Cloudinary cloudinary;

    // Upload file lên Cloudinary
    @Transactional
    public String uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Ảnh không được để trống");
        }
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return (String) uploadResult.get("secure_url");
    }

    // Xóa file trên Cloudinary dựa trên public_id
    @Transactional
    public Map deleteFile(String publicId) throws IOException {
        if (publicId != null) {
            log.info("Deleting Cloudinary file with publicId: {}", publicId);
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return result != null ? result : Map.of("result", "error");
        }
        return Map.of("result", "invalid_id");
    }


    // Trích xuất public_id từ URL Cloudinary
    @Transactional
    public String extractPublicIdFromUrl(String url) {
        try {
            // Bỏ domain & phần "/upload/"
            String path = url.substring(url.indexOf("/upload/") + 8);

            // Bỏ "v1234567890/" nếu có
            if (path.startsWith("v") && path.charAt(1) >= '0' && path.charAt(1) <= '9') {
                path = path.substring(path.indexOf("/") + 1);
            }

            // Bỏ đuôi file
            if (path.contains(".")) {
                path = path.substring(0, path.lastIndexOf('.'));
            }

            return path;
        } catch (Exception e) {
            log.error("Failed to extract publicId from URL: {}", url, e);
            return null;
        }
    }

}
