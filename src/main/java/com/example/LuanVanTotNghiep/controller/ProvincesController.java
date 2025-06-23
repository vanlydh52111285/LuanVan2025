package com.example.LuanVanTotNghiep.controller;

import com.example.LuanVanTotNghiep.dto.request.ProvincesRequest;
import com.example.LuanVanTotNghiep.dto.response.ApiResponse;
import com.example.LuanVanTotNghiep.dto.response.ProvincesResponse;
import com.example.LuanVanTotNghiep.service.ProvincesService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/provinces")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProvincesController {
    ProvincesService provincesService;

    @GetMapping("/all")
    public ApiResponse<List<ProvincesResponse>> getAllProvinces() {
        List<ProvincesResponse> result = provincesService.getAllProvinces();
        return ApiResponse.<List<ProvincesResponse>>builder()
                .code(1000)
                .message("Lấy danh sách tỉnh/thành phố thành công")
                .result(result)
                .build();
    }

    @GetMapping("/province/{province_id}")
    public ApiResponse<ProvincesResponse> getProvinceById(@PathVariable String province_id) {
        ProvincesResponse result = provincesService.getProvinceById(province_id);
        return ApiResponse.<ProvincesResponse>builder()
                .code(1000)
                .message("Lấy thông tin tỉnh/thành phố thành công")
                .result(result)
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<ProvincesResponse> createProvince(@RequestBody ProvincesRequest request) {
        ProvincesResponse result = provincesService.createProvince(request);
        return ApiResponse.<ProvincesResponse>builder()
                .code(1000)
                .message("Tạo tỉnh/thành phố thành công")
                .result(result)
                .build();
    }

    @PostMapping("/import")
    public ApiResponse<List<ProvincesResponse>> importProvinces(@RequestParam("file") MultipartFile file) {
        List<ProvincesResponse> result = provincesService.importProvincesFromExcel(file);
        return ApiResponse.<List<ProvincesResponse>>builder()
                .code(1000)
                .message("Import tỉnh/thành phố từ Excel thành công")
                .result(result)
                .build();
    }
}
