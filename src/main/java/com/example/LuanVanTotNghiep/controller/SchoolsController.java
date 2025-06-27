package com.example.LuanVanTotNghiep.controller;

import com.example.LuanVanTotNghiep.dto.request.SchoolsRequest;
import com.example.LuanVanTotNghiep.dto.response.ApiResponse;
import com.example.LuanVanTotNghiep.dto.response.SchoolsResponse;
import com.example.LuanVanTotNghiep.service.SchoolsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController

@RequestMapping("/schools")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SchoolsController {
    SchoolsService schoolsService;

    @GetMapping("/all")
    public ApiResponse<List<SchoolsResponse>> getAllSchools() {
        List<SchoolsResponse> result = schoolsService.getAllSchools();
        return ApiResponse.<List<SchoolsResponse>>builder()
                .code(1000)
                .message("Lấy tất cả trường học thành công")
                .result(result)
                .build();
    }

    @GetMapping("/province/{province_id}")
    public ApiResponse<List<SchoolsResponse>> getSchoolsByProvinceId(@PathVariable String province_id) {
        List<SchoolsResponse> result = schoolsService.getSchoolsByProvinceId(province_id);
        return ApiResponse.<List<SchoolsResponse>>builder()
                .code(1000)
                .message("Lấy danh sách trường học theo mã vùng thành công")
                .result(result)
                .build();
    }

    @PostMapping("/import")
    public ApiResponse<List<SchoolsResponse>> importSchoolsFromExcel(@RequestParam("file") MultipartFile file) {
        List<SchoolsResponse> result = schoolsService.importSchoolsFromExcel(file);
        return ApiResponse.<List<SchoolsResponse>>builder()
                .code(1000)
                .message("Import trường học từ Excel thành công")
                .result(result)
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<SchoolsResponse> createSchool(@RequestBody SchoolsRequest request) {
        SchoolsResponse result = schoolsService.createSchool(request);
        return ApiResponse.<SchoolsResponse>builder()
                .code(1000)
                .message("Tạo trường học thành công")
                .result(result)
                .build();
    }

    @DeleteMapping("/delete/{province_id}/{school_id}")
    public ApiResponse<String> deleteSchool(@PathVariable String province_id, @PathVariable String school_id){
        schoolsService.deleteSchool(province_id, school_id);
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Xóa trường học thành công")
                .build();
    }

    @DeleteMapping("/delete/list-schools/{province_id}")
    public ApiResponse<String> deleteListSchools(@PathVariable String province_id){
        schoolsService.deleteListSchools(province_id);
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Xóa trường học thành công")
                .build();
    }

    @PutMapping("/update/{province_id}/{school_id}")
    public ApiResponse<SchoolsResponse> updateSchool(@PathVariable String province_id, @PathVariable String school_id, @RequestBody SchoolsRequest request) {
        SchoolsResponse result = schoolsService.updateSchool(province_id, school_id, request);
        return ApiResponse.<SchoolsResponse>builder()
                .code(1000)
                .message("Cập nhật trường học thành công")
                .result(result)
                .build();
    }
}
