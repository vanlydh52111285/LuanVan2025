package com.example.LuanVanTotNghiep.controller;

import com.example.LuanVanTotNghiep.dto.request.SubjectsRequest;
import com.example.LuanVanTotNghiep.dto.response.ApiResponse;
import com.example.LuanVanTotNghiep.dto.response.SubjectResponse;
import com.example.LuanVanTotNghiep.service.SubjectsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/subjects")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubjectsController {

    SubjectsService subjectsService;

    @GetMapping("/all")
    public ApiResponse<List<SubjectResponse>> getAllProvinces() {
        List<SubjectResponse> result = subjectsService.getAllSubjects();
        return ApiResponse.<List<SubjectResponse>>builder()
                .code(1000)
                .message("Lấy danh sách tỉnh/thành phố thành công")
                .result(result)
                .build();
    }

    @GetMapping("/all/excludedSubIds")
    public ApiResponse<List<SubjectResponse>> getAllSubjectsExcludedSubIds() {
        Set<String> excludedIds = new HashSet<>(Arrays.asList("DBT10", "DBT11", "DBT12", "DGNL"));
        List<SubjectResponse> result = subjectsService.getAllSubjectsExcludedSubIds(excludedIds);
        return ApiResponse.<List<SubjectResponse>>builder()
                .code(1000)
                .message("Lấy danh sách tỉnh/thành phố thành công")
                .result(result)
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<SubjectResponse> createProvince(@RequestBody SubjectsRequest request) {
        SubjectResponse result = subjectsService.createSubject(request);
        return ApiResponse.<SubjectResponse>builder()
                .code(1000)
                .message("Tạo tỉnh/thành phố thành công")
                .result(result)
                .build();
    }

    @PostMapping("/import")
    public ApiResponse<List<SubjectResponse>> importProvinces(@RequestParam("file") MultipartFile file) {
        List<SubjectResponse> result = subjectsService.importSubjectsFromExcel(file);
        return ApiResponse.<List<SubjectResponse>>builder()
                .code(1000)
                .message("Import tỉnh/thành phố từ Excel thành công")
                .result(result)
                .build();
    }
}
