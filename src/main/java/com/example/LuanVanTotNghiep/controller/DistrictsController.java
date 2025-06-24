package com.example.LuanVanTotNghiep.controller;

import com.example.LuanVanTotNghiep.dto.request.DistrictsRequest;
import com.example.LuanVanTotNghiep.dto.response.ApiResponse;
import com.example.LuanVanTotNghiep.dto.response.DistrictsResponse;
import com.example.LuanVanTotNghiep.service.DistrictsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/districts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DistrictsController {
    DistrictsService districtsService;

    @GetMapping("/all")
    public ApiResponse<List<DistrictsResponse>> getAllDistricts() {
        List<DistrictsResponse> result = districtsService.getAllDistricts();
        return ApiResponse.<List<DistrictsResponse>>builder()
                .code(1000)
                .message("Lấy tất cả quận/huyện thành công")
                .result(result)
                .build();
    }

    @GetMapping("/province/{province_id}")
    public ApiResponse<List<DistrictsResponse>> getDistrictsByProvinceId(@PathVariable String province_id) {
        List<DistrictsResponse> result = districtsService.getDistrictsByProvinceId(province_id);
        return ApiResponse.<List<DistrictsResponse>>builder()
                .code(1000)
                .message("Lấy danh sách quận/huyện theo mã vùng thành công")
                .result(result)
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<DistrictsResponse> createDistrict(@RequestBody DistrictsRequest request) {
        DistrictsResponse result = districtsService.createDistrict(request);
        return ApiResponse.<DistrictsResponse>builder()
                .code(1000)
                .message("Tạo quận/huyện thành công")
                .result(result)
                .build();
    }

    @PostMapping("/import")
    public ApiResponse<List<DistrictsResponse>> importDistricts(@RequestParam("file") MultipartFile file) {
        List<DistrictsResponse> result = districtsService.importDistrictsFromExcel(file);
        return ApiResponse.<List<DistrictsResponse>>builder()
                .code(1000)
                .message("Import quận/huyện từ Excel thành công")
                .result(result)
                .build();
    }
}
