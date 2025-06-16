package com.example.LuanVanTotNghiep.service;

import com.cloudinary.Cloudinary;
import com.example.LuanVanTotNghiep.dto.request.DocumentsRequest;
import com.example.LuanVanTotNghiep.dto.response.DocumentsResponse;
import com.example.LuanVanTotNghiep.entity.Applications;
import com.example.LuanVanTotNghiep.entity.Documents;
import com.example.LuanVanTotNghiep.entity.Users;
import com.example.LuanVanTotNghiep.exception.AppException;
import com.example.LuanVanTotNghiep.exception.ErrorCode;
import com.example.LuanVanTotNghiep.mapper.DocumentsMapper;
import com.example.LuanVanTotNghiep.repository.ApplicationsRepository;
import com.example.LuanVanTotNghiep.repository.DocumentsRepository;
import com.example.LuanVanTotNghiep.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DocumentsService {
    @Autowired
    DocumentsRepository documentRepository;

    @Autowired
    ApplicationsRepository applicationRepository;

    @Autowired
    Cloudinary cloudinary;

    @Autowired
    UsersRepository userRepository;

    @Autowired
    CloudinaryService cloudinaryService;

    @Autowired
    DocumentsMapper documentsMapper;

    @Autowired
    AuthenticationService authenticationService;

    public Documents findById(Integer id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
    }

    public List<Documents> getDocumentsByUserIdAndApplicationId(String userId, String applicationId) {
        if (userId == null || userId.isEmpty() || applicationId == null || applicationId.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
        List<Documents> documents = documentRepository.findByUserIdAndApplicationId(userId, applicationId);
        if (documents.isEmpty()) {
            throw new AppException(ErrorCode.DATA_NOT_FOUND);
        }
        return documents;
    }

    @Transactional
    public DocumentsResponse createDocument(DocumentsRequest request, MultipartFile[] files) throws IOException {
        log.info("Tạo document với request: {}", request);
        String userId = authenticationService.getAuthenticatedUserId();

        Applications application = null;
        if (request.getApplicationId() != null && !request.getApplicationId().isEmpty()) {
            application = applicationRepository.findById(request.getApplicationId())
                    .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
            if (!application.getUser().getUser_id().equals(userId)) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
        }

        validateFileTypes(files);
        Documents document = documentsMapper.toCreateDocuments(request);
        document.setUser(new Users(userId));
        if (application != null) {
            document.setApplication(application);
        }
        setDocumentLinks(document, files);
        Documents saved = documentRepository.saveAndFlush(document);
        log.info("Đã lưu document: id={}, applicationId={}", saved.getDocument_id(), saved.getApplication() != null ? saved.getApplication().getApplication_id() : null);

        DocumentsResponse response = documentsMapper.toDocumentsResponse(saved);
        response.setApplicationId(saved.getApplication() != null ? saved.getApplication().getApplication_id() : null);
        response.setUserId(saved.getUser() != null ? saved.getUser().getUser_id() : null);
        log.info("Response: {}", response);
        return response;
    }

    @Transactional
    public DocumentsResponse updateDocument(Integer documentId, DocumentsRequest request, MultipartFile[] files) throws IOException {
        log.info("Cập nhật document ID: {} với request: {}", documentId, request);
        String userId = authenticationService.getAuthenticatedUserId();
        Documents document = documentRepository.findById(documentId)
                .orElseThrow(() -> new AppException(ErrorCode.CONTENT_NO_EXISTS));

        if (!document.getUser().getUser_id().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if (request.getApplicationId() != null && !request.getApplicationId().isEmpty()) {
            Applications application = applicationRepository.findById(request.getApplicationId())
                    .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
            if (!application.getUser().getUser_id().equals(userId)) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
            document.setApplication(application);
        } else {
            document.setApplication(null);
        }

        validateFileTypes(files);
        documentsMapper.updateDocuments(document, request);
        setDocumentLinks(document, files);
        Documents updated = documentRepository.saveAndFlush(document);
        log.info("Đã cập nhật document: id={}, applicationId={}", updated.getDocument_id(), updated.getApplication() != null ? updated.getApplication().getApplication_id() : null);

        DocumentsResponse response = documentsMapper.toDocumentsResponse(updated);
        response.setApplicationId(updated.getApplication() != null ? updated.getApplication().getApplication_id() : null);
        response.setUserId(updated.getUser() != null ? updated.getUser().getUser_id() : null);
        log.info("Response: {}", response);
        return response;
    }

    @Transactional
    public void deleteDocument(Integer documentId, String userId) throws IOException {
        log.info("Xóa document với ID: {} cho userId: {}", documentId, userId);
        Documents document = documentRepository.findByDocumentIdAndUserId(documentId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        deleteDocumentLinks(document);
        documentRepository.delete(document);
        log.info("Đã xóa document với ID: {}", documentId);
    }

    public void deleteAllDocumentsByApplicationId(String userId, String applicationId) {
        List<Documents> documents = documentRepository.findByUserIdAndApplicationId(userId, applicationId);
        for (Documents doc : documents) {
            try {
                deleteDocument(doc.getDocument_id(), userId);
            } catch (IOException e) {
                log.error("Lỗi khi xóa tài liệu ID: {}", doc.getDocument_id(), e);
                throw new AppException(ErrorCode.UNEXPECTED_ERROR);
            }
        }
    }

    @Transactional
    public void updateDocumentsForApplication(Applications application) {
        String userId = application.getUser().getUser_id();
        if (userId == null || userId.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
        List<Documents> documents = documentRepository.findByApplicationIsNullAndUserId(userId);
        documents.forEach(doc -> {
            doc.setApplication(application);
            documentRepository.save(doc);
        });
    }

    private void validateFileTypes(MultipartFile[] files) {
        if (files == null || files.length != 8) {
            log.error("Số lượng file không đúng: {}", files == null ? "null" : files.length);
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
        boolean hasAtLeastOneFile = false;
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            if (file != null && !file.isEmpty()) {
                if (!file.getContentType().startsWith("image/")) {
                    throw new AppException(ErrorCode.INVALID_REQUEST);
                }
                log.info("File {}: name={}, size={}, contentType={}", i, file.getOriginalFilename(), file.getSize(), file.getContentType());
                hasAtLeastOneFile = true;
            } else {
                log.info("File {} is null or empty", i);
            }
        }
        if (!hasAtLeastOneFile) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
    }

    private void setDocumentLinks(Documents document, MultipartFile[] files) throws IOException {
        document.setDocument_link_cccd(uploadFile(files[0], "cccd"));
        document.setDocument_link_hoc_ba_lop10(uploadFile(files[1], "hoc_ba_lop10"));
        document.setDocument_link_hoc_ba_lop11(uploadFile(files[2], "hoc_ba_lop11"));
        document.setDocument_link_hoc_ba_lop12(uploadFile(files[3], "hoc_ba_lop12"));
        document.setDocument_link_bang_tot_nghiep_thpt(uploadFile(files[4], "bang_tot_nghiep_thpt"));
        document.setDocument_link_ket_qua_thi_thpt(uploadFile(files[5], "ket_qua_thi_thpt"));
        document.setDocument_link_ket_qua_thi_dgnl(uploadFile(files[6], "ket_qua_thi_dgnl"));
        document.setDocument_link_chung_chi_ngoai_ngu(uploadFile(files[7], "chung_chi_ngoai_ngu"));
    }

    private String uploadFile(MultipartFile file, String fileType) throws IOException {
        if (file == null || file.isEmpty()) {
            log.info("File {} is null or empty, skipping upload", fileType);
            return null;
        }
        try {
            String url = cloudinaryService.uploadFile(file);
            log.info("Uploaded {}: {}", fileType, url);
            return url;
        } catch (IOException e) {
            log.error("Failed to upload {}: {}", fileType, e.getMessage());
            throw new AppException(ErrorCode.UNEXPECTED_ERROR);
        }
    }

    private void deleteDocumentLinks(Documents document) throws IOException {
        List<String> failedUrls = new ArrayList<>();
        Stream.of(
                        document.getDocument_link_cccd(),
                        document.getDocument_link_hoc_ba_lop10(),
                        document.getDocument_link_hoc_ba_lop11(),
                        document.getDocument_link_hoc_ba_lop12(),
                        document.getDocument_link_bang_tot_nghiep_thpt(),
                        document.getDocument_link_ket_qua_thi_thpt(),
                        document.getDocument_link_ket_qua_thi_dgnl(),
                        document.getDocument_link_chung_chi_ngoai_ngu()
                ).filter(url -> url != null && !url.isEmpty())
                .forEach(url -> {
                    try {
                        String publicId = cloudinaryService.extractPublicIdFromUrl(url);
                        if (publicId != null) {
                            Map result = cloudinaryService.deleteFile(publicId);
                            if (!"ok".equals(result.get("result")) && !"not found".equals(result.get("result"))) {
                                failedUrls.add(url);
                                log.error("Failed to delete file on Cloudinary: {}, result: {}", url, result);
                            }
                        } else {
                            failedUrls.add(url);
                            log.error("Invalid publicId for URL: {}", url);
                        }
                    } catch (Exception e) {
                        failedUrls.add(url);
                        log.error("Failed to delete file: {}", url, e);
                    }
                });
        if (!failedUrls.isEmpty()) {
            throw new IOException("Failed to delete files on Cloudinary: " + String.join(", ", failedUrls));
        }
    }
}
