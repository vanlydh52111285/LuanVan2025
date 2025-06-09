package com.example.LuanVanTotNghiep.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    @Autowired
    private Cloudinary cloudinary;

    // Upload file lên Cloudinary
    public String uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Ảnh không được để trống");
        }
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return (String) uploadResult.get("secure_url");
    }

    // Xóa file trên Cloudinary dựa trên public_id
    public void deleteFile(String publicId) throws IOException {
        if (publicId != null) {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        }
    }

    // Trích xuất public_id từ URL Cloudinary
    public String extractPublicIdFromUrl(String url) {
        if (url == null) return null;
        String[] parts = url.split("/upload/");
        if (parts.length < 2) return null;
        String publicIdWithExtension = parts[1];
        return publicIdWithExtension.substring(0, publicIdWithExtension.lastIndexOf('.'));
    }
}
