package com.example.LuanVanTotNghiep.controller;

import com.example.LuanVanTotNghiep.dto.request.DocumentsRequest;
import com.example.LuanVanTotNghiep.dto.response.ApiResponse;
import com.example.LuanVanTotNghiep.dto.response.DocumentsResponse;
import com.example.LuanVanTotNghiep.entity.Documents;
import com.example.LuanVanTotNghiep.entity.Users;
import com.example.LuanVanTotNghiep.exception.AppException;
import com.example.LuanVanTotNghiep.exception.ErrorCode;
import com.example.LuanVanTotNghiep.mapper.DocumentsMapper;
import com.example.LuanVanTotNghiep.repository.UsersRepository;
import com.example.LuanVanTotNghiep.service.ApplicationsService;
import com.example.LuanVanTotNghiep.service.AuthenticationService;
import com.example.LuanVanTotNghiep.service.DocumentsService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DocumentController {
    @Autowired
    DocumentsService documentService;

    @Autowired
    ApplicationsService applicationService;

    @Autowired
    DocumentsMapper documentsMapper;

    @Autowired
    AuthenticationService authenticationService;

    @GetMapping("/images")
    public ApiResponse<List<DocumentsResponse>> getDocumentImages(
            @RequestParam String applicationId) {
        String userId = authenticationService.getAuthenticatedUserId();
        ApiResponse<List<DocumentsResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setCode(1000);
        apiResponse.setMessage("Lấy tài liệu thành công");
        apiResponse.setResult(documentService.getDocumentsByUserIdAndApplicationId(userId, applicationId)
                .stream()
                .map(documentsMapper::toDocumentsResponse)
                .toList());
        return apiResponse;
    }

    @PostMapping("/create")
    public ApiResponse<DocumentsResponse> createDocument(
            @Valid @ModelAttribute DocumentsRequest request,
            @RequestParam(value = "cccdFile", required = false) MultipartFile cccdFile,
            @RequestParam(value = "hocBaLop10File", required = false) MultipartFile hocBaLop10File,
            @RequestParam(value = "hocBaLop11File", required = false) MultipartFile hocBaLop11File,
            @RequestParam(value = "hocBaLop12File", required = false) MultipartFile hocBaLop12File,
            @RequestParam(value = "bangTotNghiepThptFile", required = false) MultipartFile bangTotNghiepThptFile,
            @RequestParam(value = "ketQuaThiThptFile", required = false) MultipartFile ketQuaThiThptFile,
            @RequestParam(value = "ketQuaThiDgnlFile", required = false) MultipartFile ketQuaThiDgnlFile,
            @RequestParam(value = "chungChiNgoaiNguFile", required = false) MultipartFile chungChiNgoaiNguFile) throws IOException {
        MultipartFile[] files = {cccdFile, hocBaLop10File, hocBaLop11File, hocBaLop12File,
                bangTotNghiepThptFile, ketQuaThiThptFile, ketQuaThiDgnlFile, chungChiNgoaiNguFile};
        ApiResponse<DocumentsResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(1000);
        apiResponse.setMessage("Tạo tài liệu thành công");
        apiResponse.setResult(documentService.createDocument(request, files));
        return apiResponse;
    }

    @DeleteMapping("delete/{documentId}")
    public ApiResponse<String> deleteDocument(@PathVariable Integer documentId) throws IOException {
        String userId = authenticationService.getAuthenticatedUserId();
        documentService.deleteDocument(documentId, userId);
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setCode(1000);
        apiResponse.setMessage("Xóa tài liệu thành công");
        apiResponse.setResult("Document with ID " + documentId + " deleted successfully");
        return apiResponse;
    }

    @PutMapping("/update/{documentId}")
    public ApiResponse<DocumentsResponse> updateDocument(
            @PathVariable Integer documentId,
            @Valid @ModelAttribute DocumentsRequest request,
            @RequestParam(value = "cccdFile", required = false) MultipartFile cccdFile,
            @RequestParam(value = "hocBaLop10File", required = false) MultipartFile hocBaLop10File,
            @RequestParam(value = "hocBaLop11File", required = false) MultipartFile hocBaLop11File,
            @RequestParam(value = "hocBaLop12File", required = false) MultipartFile hocBaLop12File,
            @RequestParam(value = "bangTotNghiepThptFile", required = false) MultipartFile bangTotNghiepThptFile,
            @RequestParam(value = "ketQuaThiThptFile", required = false) MultipartFile ketQuaThiThptFile,
            @RequestParam(value = "ketQuaThiDgnlFile", required = false) MultipartFile ketQuaThiDgnlFile,
            @RequestParam(value = "chungChiNgoaiNguFile", required = false) MultipartFile chungChiNgoaiNguFile) throws IOException {
        MultipartFile[] files = {cccdFile, hocBaLop10File, hocBaLop11File, hocBaLop12File,
                bangTotNghiepThptFile, ketQuaThiThptFile, ketQuaThiDgnlFile, chungChiNgoaiNguFile};
        ApiResponse<DocumentsResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(1000);
        apiResponse.setMessage("Cập nhật tài liệu thành công");
        apiResponse.setResult(documentService.updateDocument(documentId, request, files));
        return apiResponse;
    }
}
