package com.example.LuanVanTotNghiep.controller;

import com.example.LuanVanTotNghiep.dto.request.ApplicationsRequest;
import com.example.LuanVanTotNghiep.dto.response.ApiResponse;
import com.example.LuanVanTotNghiep.dto.response.ApplicationsResponse;
import com.example.LuanVanTotNghiep.entity.Applications;
import com.example.LuanVanTotNghiep.entity.Methods;
import com.example.LuanVanTotNghiep.entity.Users;
import com.example.LuanVanTotNghiep.service.ApplicationsService;
import com.example.LuanVanTotNghiep.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationController {
    @Autowired
    ApplicationsService applicationService;

    @Autowired
    AuthenticationService authenticationService;

    @GetMapping("/getAllApplications")
    public ApiResponse<List<ApplicationsResponse>> getAllApplications() {
        ApiResponse<List<ApplicationsResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Applications retrieved successfully");
        apiResponse.setResult(applicationService.getAllApplications());
        return apiResponse;
    }

    @GetMapping("/getApplicationsByUser")
    public ApiResponse<List<ApplicationsResponse>> getApplicationsByUser() {
        String userId = authenticationService.getAuthenticatedUserId();
        List<ApplicationsResponse> applications = applicationService.getApplicationsByUserId(userId);

        ApiResponse<List<ApplicationsResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setCode(1000);
        if (applications.isEmpty()) {
            apiResponse.setMessage("Danh sách hồ sơ trống");
        } else {
            apiResponse.setMessage("Lấy danh sách hồ sơ theo người dùng thành công");
        }
        apiResponse.setResult(applications);
        return apiResponse;
    }



    @PostMapping("/create")
    public ApiResponse<ApplicationsResponse> createApplication(@Valid @RequestBody ApplicationsRequest request) {
        ApiResponse<ApplicationsResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(1000);
        apiResponse.setMessage("Tạo hồ sơ thành công");
        apiResponse.setResult(applicationService.createApplication(request));
        return apiResponse;
    }

    @DeleteMapping("/delete/{applicationId}")
    public ApiResponse<Void> deleteApplication(@PathVariable String applicationId) {
        applicationService.deleteApplication(applicationId);
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(1000);
        apiResponse.setMessage("Xóa hồ sơ thành công");
        return apiResponse;
    }

    @PutMapping("/update/{applicationId}")
    public ApiResponse<ApplicationsResponse> updateApplication(
            @PathVariable String applicationId,
            @Valid @RequestBody ApplicationsRequest request) {
        ApiResponse<ApplicationsResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(1000);
        apiResponse.setMessage("Cập nhật hồ sơ thành công");
        apiResponse.setResult(applicationService.updateApplication(applicationId, request));
        return apiResponse;
    }
}
