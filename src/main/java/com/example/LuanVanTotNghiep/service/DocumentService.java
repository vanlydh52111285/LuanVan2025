package com.example.LuanVanTotNghiep.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.LuanVanTotNghiep.entity.Applications;
import com.example.LuanVanTotNghiep.entity.Documents;
import com.example.LuanVanTotNghiep.entity.Users;
import com.example.LuanVanTotNghiep.repository.ApplicationRepository;
import com.example.LuanVanTotNghiep.repository.DocumentRepository;
import com.example.LuanVanTotNghiep.repository.UsersRepository;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

public class DocumentService {
    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    public Documents getDocumentById(int id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Document with ID " + id + " not found."));
    }

    public List<Documents> getDocumentsByUserIdAndApplicationId(Long userId, String applicationId) {
        return documentRepository.findByUserIdAndApplicationId(userId, applicationId);
    }

    @Transactional
    public Documents createDocument(
            MultipartFile cccdFile, MultipartFile hocBaLop10File, MultipartFile hocBaLop11File,
            MultipartFile hocBaLop12File, MultipartFile bangTotNghiepThptFile, MultipartFile ketQuaThiThptFile,
            MultipartFile ketQuaThiDgnlFile, MultipartFile chungChiNgoaiNguFile,
            String applicationId, Long userId) throws IOException {
        Users user = userRepository.findById(String.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("User ID " + userId + " not found."));
        Applications application = null;
        if (applicationId != null && !applicationId.isEmpty()) {
            application = applicationRepository.findById(applicationId)
                    .orElseThrow(() -> new IllegalArgumentException("Application ID " + applicationId + " not found."));
        }

        Documents document = Documents.builder()
                .application(application)
                .user(user)
                .document_link_cccd(uploadFile(cccdFile))
                .document_link_hoc_ba_lop10(uploadFile(hocBaLop10File))
                .document_link_hoc_ba_lop11(uploadFile(hocBaLop11File))
                .document_link_hoc_ba_lop12(uploadFile(hocBaLop12File))
                .document_link_bang_tot_nghiep_thpt(uploadFile(bangTotNghiepThptFile))
                .document_link_ket_qua_thi_thpt(uploadFile(ketQuaThiThptFile))
                .document_link_ket_qua_thi_dgnl(uploadFile(ketQuaThiDgnlFile))
                .document_link_chung_chi_ngoai_ngu(uploadFile(chungChiNgoaiNguFile))
                .build();

        return documentRepository.save(document);
    }

    @Transactional
    public Documents updateDocument(
            int documentId,
            String cccdUrl, MultipartFile cccdFile,
            String hocBaLop10Url, MultipartFile hocBaLop10File,
            String hocBaLop11Url, MultipartFile hocBaLop11File,
            String hocBaLop12Url, MultipartFile hocBaLop12File,
            String bangTotThptUrl, MultipartFile bangTotThptFile,
            String ketQuaThptUrl, MultipartFile ketQuaThptFile,
            String ketQuaDgnlUrl, MultipartFile ketQuaDgnlFile,
            String chungChiNgoaiNguUrl, MultipartFile chungChiNgoaiNguFile,
            String applicationId) throws IOException {
        Documents document = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Document ID " + documentId + " not found."));

        if (applicationId != null && !applicationId.isEmpty()) {
            document.setApplication(applicationRepository.findById(applicationId)
                    .orElseThrow(() -> new IllegalArgumentException("Application ID " + applicationId + " not found.")));
        } else {
            document.setApplication(null);
        }

        updateFile(document::getDocument_link_cccd, document::setDocument_link_cccd, cccdUrl, cccdFile);
        updateFile(document::getDocument_link_hoc_ba_lop10, document::setDocument_link_hoc_ba_lop10, hocBaLop10Url, hocBaLop10File);
        updateFile(document::getDocument_link_hoc_ba_lop11, document::setDocument_link_hoc_ba_lop11, hocBaLop11Url, hocBaLop11File);
        updateFile(document::getDocument_link_hoc_ba_lop12, document::setDocument_link_hoc_ba_lop12, hocBaLop12Url, hocBaLop12File);
        updateFile(document::getDocument_link_bang_tot_nghiep_thpt, document::setDocument_link_bang_tot_nghiep_thpt, bangTotThptUrl, bangTotThptFile);
        updateFile(document::getDocument_link_ket_qua_thi_thpt, document::setDocument_link_ket_qua_thi_thpt, ketQuaThptUrl, ketQuaThptFile);
        updateFile(document::getDocument_link_ket_qua_thi_dgnl, document::setDocument_link_ket_qua_thi_dgnl, ketQuaDgnlUrl, ketQuaDgnlFile);
        updateFile(document::getDocument_link_chung_chi_ngoai_ngu, document::setDocument_link_chung_chi_ngoai_ngu, chungChiNgoaiNguUrl, chungChiNgoaiNguFile);

        return documentRepository.save(document);
    }

    private String uploadFile(MultipartFile file) throws IOException {
        return cloudinaryService.uploadFile(file);
    }

    private void updateFile(java.util.function.Supplier<String> getter, java.util.function.Consumer<String> setter,
                            String clientUrl, MultipartFile file) throws IOException {
        String currentUrl = getter.get();
        if (clientUrl == null) {
            cloudinaryService.deleteFile(currentUrl);
            setter.accept(null);
        } else if (!clientUrl.equals(currentUrl) && file != null && !file.isEmpty()) {
            cloudinaryService.deleteFile(currentUrl);
            setter.accept(cloudinaryService.uploadFile(file));
        }
    }

    @Transactional
    public void deleteDocument(int documentId) throws IOException {
        Documents document = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài liệu ảnh có mã " + documentId + "."));
        cloudinaryService.deleteFile(document.getDocument_link_cccd());
        cloudinaryService.deleteFile(document.getDocument_link_hoc_ba_lop10());
        cloudinaryService.deleteFile(document.getDocument_link_hoc_ba_lop11());
        cloudinaryService.deleteFile(document.getDocument_link_hoc_ba_lop12());
        cloudinaryService.deleteFile(document.getDocument_link_bang_tot_nghiep_thpt());
        cloudinaryService.deleteFile(document.getDocument_link_ket_qua_thi_thpt());
        cloudinaryService.deleteFile(document.getDocument_link_ket_qua_thi_dgnl());
        cloudinaryService.deleteFile(document.getDocument_link_chung_chi_ngoai_ngu());
        documentRepository.deleteById(documentId);
    }

    @Transactional
    public void updateDocumentsForApplication(Applications application) {
        Long userId = Long.valueOf(application.getUser().getUser_id());
        List<Documents> documents = documentRepository.findByApplicationIsNullAndUserId(userId);
        for (Documents doc : documents) {
            doc.setApplication(application);
            documentRepository.save(doc);
        }
    }
}
