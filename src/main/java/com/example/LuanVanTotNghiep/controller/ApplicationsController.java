package com.example.LuanVanTotNghiep.controller;

import com.example.LuanVanTotNghiep.dto.request.ApplicationsRequest;
import com.example.LuanVanTotNghiep.dto.response.ApiResponse;
import com.example.LuanVanTotNghiep.dto.response.ApplicationsResponse;
import com.example.LuanVanTotNghiep.service.ApplicationsService;
import com.example.LuanVanTotNghiep.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationsController {
    ApplicationsService applicationService;
    AuthenticationService authenticationService;

    @GetMapping("/getAllApplications")
    public ApiResponse<List<ApplicationsResponse>> getAllApplications() {
        return ApiResponse.<List<ApplicationsResponse>>builder()
                .code(1000)
                .message("Lấy danh sách hồ sơ thành công")
                .result(applicationService.getAllApplications())
                .build();
    }

    @GetMapping("/getApplicationsByUser")
    public ApiResponse<List<ApplicationsResponse>> getApplicationsByUser() {
        String userId = authenticationService.getAuthenticatedUserId();
        List<ApplicationsResponse> applications = applicationService.getApplicationsByUserId(userId);
        return ApiResponse.<List<ApplicationsResponse>>builder()
                .code(1000)
                .message(applications.isEmpty() ? "Danh sách hồ sơ trống" : "Lấy danh sách hồ sơ theo người dùng thành công")
                .result(applications)
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<ApplicationsResponse> createApplication(@Valid @RequestBody ApplicationsRequest request) {
        return ApiResponse.<ApplicationsResponse>builder()
                .code(1000)
                .message("Tạo hồ sơ thành công")
                .result(applicationService.createApplication(request))
                .build();
    }

    @DeleteMapping("/delete/{applicationId}")
    public ApiResponse<Void> deleteApplication(@PathVariable String applicationId) {
        applicationService.deleteApplication(applicationId);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Xóa hồ sơ thành công")
                .build();
    }

    @PutMapping("/update/{applicationId}")
    public ApiResponse<ApplicationsResponse> updateApplication(
            @PathVariable String applicationId,
            @Valid @RequestBody ApplicationsRequest request) {
        return ApiResponse.<ApplicationsResponse>builder()
                .code(1000)
                .message("Cập nhật hồ sơ thành công")
                .result(applicationService.updateApplication(applicationId, request))
                .build();
    }
}