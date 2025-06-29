package com.example.LuanVanTotNghiep.service;

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
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
    DocumentsRepository documentRepository;
    ApplicationsRepository applicationRepository;
    CloudinaryService cloudinaryService;
    DocumentsMapper documentsMapper;
    AuthenticationService authenticationService;
    ApplicationsRepository applicationsRepository;

    public Documents findById(Integer id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
    }

    public List<Documents> getDocumentsByUserIdAndApplicationId(String applicationId) {
        List<Documents> documents = documentRepository.findByApplicationId(applicationId);
        if (documents.isEmpty()) {
            throw new AppException(ErrorCode.DATA_NOT_FOUND);
        }
        return documents;
    }

    @Transactional
    public DocumentsResponse createDocument(DocumentsRequest request, MultipartFile[] files) throws IOException {
        log.info("Tạo document với request: {}", request);
        String userId = authenticationService.getAuthenticatedUserId();

        Applications application = applicationsRepository.findById(request.getApplicationId())
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND, "Không tìm thấy đơn xét tuyển với mã: " + request.getApplicationId()));

        if (!application.getUser().getUser_id().equals(userId)) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
        }


        validateFileTypes(files);
        Documents document = documentsMapper.toCreateDocuments(request);
        document.setApplication(application);
        setDocumentLinks(document, files);
        Documents saved = documentRepository.saveAndFlush(document);

        DocumentsResponse response = documentsMapper.toDocumentsResponse(saved);
        response.setApplicationId(saved.getApplication() != null ? saved.getApplication().getApplication_id() : null);
        return response;
    }

    @Transactional
    public DocumentsResponse updateDocument(Integer documentId, DocumentsRequest request, MultipartFile[] files) throws IOException {
        String userId = authenticationService.getAuthenticatedUserId();
        Documents document = documentRepository.findById(documentId)
                .orElseThrow(() -> new AppException(ErrorCode.CONTENT_NO_EXISTS));

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

        DocumentsResponse response = documentsMapper.toDocumentsResponse(updated);
        response.setApplicationId(updated.getApplication() != null ? updated.getApplication().getApplication_id() : null);
        return response;
    }

    @Transactional
    public void deleteDocument(String applicationId, int documentId) throws IOException {
        // Tìm Documents cần xóa
        Documents document = documentRepository.findByApplicationIdAndDocumentId(applicationId, documentId)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        // Tìm Applications liên quan
        Applications application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));

        // Xóa liên kết giữa Applications và Documents
        if (application.getDocuments() != null && application.getDocuments().getDocument_id() == documentId) {
            application.setDocuments(null); // Đặt liên kết về null
            applicationRepository.saveAndFlush(application); // Cập nhật trước khi xóa
        }

        // Xóa Documents
        documentRepository.delete(document);
        documentRepository.flush();

        // Xử lý xóa file trên Cloudinary, không ảnh hưởng đến giao dịch
        try {
            deleteDocumentLinks(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void deleteAllDocumentsByApplicationId(String applicationId) {
        List<Documents> documents = documentRepository.findByApplicationId(applicationId);
        for (Documents doc : documents) {
            try {
                deleteDocument(applicationId, doc.getDocument_id());
            } catch (IOException e) {
                throw new AppException(ErrorCode.UNEXPECTED_ERROR);
            }
        }
    }

    private void validateFileTypes(MultipartFile[] files) {
        if (files == null || files.length != 8) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
        boolean hasAtLeastOneFile = false;
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            if (file != null && !file.isEmpty()) {
                if (!file.getContentType().startsWith("image/")) {
                    throw new AppException(ErrorCode.INVALID_REQUEST);
                }
                hasAtLeastOneFile = true;
            } else {
            }
        }
        if (!hasAtLeastOneFile) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
    }

    private void setDocumentLinks(Documents document, MultipartFile[] files) throws IOException {
        document.setCccd(uploadFile(files[0], "cccd"));
        document.setHoc_ba_lop10(uploadFile(files[1], "hoc_ba_lop10"));
        document.setHoc_ba_lop11(uploadFile(files[2], "hoc_ba_lop11"));
        document.setHoc_ba_lop12(uploadFile(files[3], "hoc_ba_lop12"));
        document.setBang_tot_nghiep_thpt(uploadFile(files[4], "bang_tot_nghiep_thpt"));
        document.setKet_qua_thi_thpt(uploadFile(files[5], "ket_qua_thi_thpt"));
        document.setKet_qua_thi_dgnl(uploadFile(files[6], "ket_qua_thi_dgnl"));
        document.setChung_chi_ngoai_ngu(uploadFile(files[7], "chung_chi_ngoai_ngu"));
    }

    private String uploadFile(MultipartFile file, String fileType) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            String url = cloudinaryService.uploadFile(file);
            return url;
        } catch (IOException e) {
            throw new AppException(ErrorCode.UNEXPECTED_ERROR);
        }
    }

    private void deleteDocumentLinks(Documents document) throws IOException {
        List<String> failedUrls = new ArrayList<>();
        Stream.of(
                        document.getCccd(),
                        document.getHoc_ba_lop10(),
                        document.getHoc_ba_lop11(),
                        document.getHoc_ba_lop12(),
                        document.getBang_tot_nghiep_thpt(),
                        document.getKet_qua_thi_thpt(),
                        document.getKet_qua_thi_dgnl(),
                        document.getChung_chi_ngoai_ngu()
                ).filter(url -> url != null && !url.isEmpty())
                .forEach(url -> {
                    try {
                        String publicId = cloudinaryService.extractPublicIdFromUrl(url);
                        if (publicId != null) {
                            Map result = cloudinaryService.deleteFile(publicId);
                            if (!"ok".equals(result.get("result")) && !"not found".equals(result.get("result"))) {
                                failedUrls.add(url);
                            }
                        } else {
                            failedUrls.add(url);
                        }
                    } catch (Exception e) {
                        failedUrls.add(url);
                    }
                });
        if (!failedUrls.isEmpty()) {
            throw new IOException("Failed to delete files on Cloudinary: " + String.join(", ", failedUrls));
        }
    }
}
