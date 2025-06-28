package com.example.LuanVanTotNghiep.controller;

import com.example.LuanVanTotNghiep.dto.request.DocumentsRequest;
import com.example.LuanVanTotNghiep.dto.response.ApiResponse;
import com.example.LuanVanTotNghiep.dto.response.DocumentsResponse;
import com.example.LuanVanTotNghiep.mapper.DocumentsMapper;
import com.example.LuanVanTotNghiep.service.ApplicationsService;
import com.example.LuanVanTotNghiep.service.AuthenticationService;
import com.example.LuanVanTotNghiep.service.DocumentsService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DocumentsController {
    DocumentsService documentService;
    ApplicationsService applicationService;
    DocumentsMapper documentsMapper;
    AuthenticationService authenticationService;

    @GetMapping("/images")
    public ApiResponse<List<DocumentsResponse>> getDocumentImages(
            @RequestParam String applicationId) {
        String userId = authenticationService.getAuthenticatedUserId();
        return ApiResponse.<List<DocumentsResponse>>builder()
                .code(1000)
                .message("Lấy tài liệu thành công")
                .result(documentService.getDocumentsByUserIdAndApplicationId(userId, applicationId)
                        .stream()
                        .map(doc -> {
                            DocumentsResponse response = documentsMapper.toDocumentsResponse(doc);
                            response.setApplicationId(doc.getApplication() != null ? doc.getApplication().getApplication_id() : null);
                            response.setUserId(doc.getUser() != null ? doc.getUser().getUser_id() : null);
                            return response;
                        })
                        .toList())
                .build();
    }

    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ApiResponse<DocumentsResponse> createDocument(
            @Valid @ModelAttribute DocumentsRequest request,
            @RequestParam("cccdFile") MultipartFile cccdFile,
            @RequestParam("hocBaLop10File") MultipartFile hocBaLop10File,
            @RequestParam("hocBaLop11File") MultipartFile hocBaLop11File,
            @RequestParam("hocBaLop12File") MultipartFile hocBaLop12File,
            @RequestParam(value = "bangTotNghiepThptFile", required = false) MultipartFile bangTotNghiepThptFile,
            @RequestParam(value = "ketQuaThiThptFile", required = false) MultipartFile ketQuaThiThptFile,
            @RequestParam(value = "ketQuaThiDgnlFile", required = false) MultipartFile ketQuaThiDgnlFile,
            @RequestParam(value = "chungChiNgoaiNguFile", required = false) MultipartFile chungChiNgoaiNguFile) throws IOException {
        MultipartFile[] files = {cccdFile, hocBaLop10File, hocBaLop11File, hocBaLop12File,
                bangTotNghiepThptFile, ketQuaThiThptFile, ketQuaThiDgnlFile, chungChiNgoaiNguFile};
        return ApiResponse.<DocumentsResponse>builder()
                .code(1000)
                .message("Tạo tài liệu thành công")
                .result(documentService.createDocument(request, files))
                .build();
    }

    @DeleteMapping("/delete/{documentId}")
    public ApiResponse<String> deleteDocument(@PathVariable Integer documentId) throws IOException {
        String userId = authenticationService.getAuthenticatedUserId();
        documentService.deleteDocument(documentId, userId);
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Xóa tài liệu thành công")
                .result("Document with ID " + documentId + " deleted successfully")
                .build();
    }

    @PutMapping(value = "/update/{documentId}", consumes = "multipart/form-data")
    public ApiResponse<DocumentsResponse> updateDocument(
            @PathVariable Integer documentId,
            @Valid @ModelAttribute DocumentsRequest request,
            @RequestParam("cccdFile") MultipartFile cccdFile,
            @RequestParam("hocBaLop10File") MultipartFile hocBaLop10File,
            @RequestParam("hocBaLop11File") MultipartFile hocBaLop11File,
            @RequestParam("hocBaLop12File") MultipartFile hocBaLop12File,
            @RequestParam(value = "bangTotNghiepThptFile", required = false) MultipartFile bangTotNghiepThptFile,
            @RequestParam(value = "ketQuaThiThptFile", required = false) MultipartFile ketQuaThiThptFile,
            @RequestParam(value = "ketQuaThiDgnlFile", required = false) MultipartFile ketQuaThiDgnlFile,
            @RequestParam(value = "chungChiNgoaiNguFile", required = false) MultipartFile chungChiNgoaiNguFile) throws IOException {
        MultipartFile[] files = {cccdFile, hocBaLop10File, hocBaLop11File, hocBaLop12File,
                bangTotNghiepThptFile, ketQuaThiThptFile, ketQuaThiDgnlFile, chungChiNgoaiNguFile};
        return ApiResponse.<DocumentsResponse>builder()
                .code(1000)
                .message("Cập nhật tài liệu thành công")
                .result(documentService.updateDocument(documentId, request, files))
                .build();
    }

}
