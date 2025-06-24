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

import java.util.List;

@RestController
@RequestMapping("/subjects")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubjectsController {

    SubjectsService subjectsService;

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
