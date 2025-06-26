package com.example.LuanVanTotNghiep.controller;

import com.example.LuanVanTotNghiep.dto.request.DistrictsRequest;
import com.example.LuanVanTotNghiep.dto.request.ProvincesRequest;
import com.example.LuanVanTotNghiep.dto.response.ApiResponse;
import com.example.LuanVanTotNghiep.dto.response.DistrictsResponse;
import com.example.LuanVanTotNghiep.dto.response.ProvincesResponse;
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
        List<DistrictsResponse> result = districtsService.getListDistrictsByProvinceId(province_id);
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


    @DeleteMapping("/delete/{province_id}/{id}")
    public ApiResponse<String> deleteDistrict(@PathVariable String province_id, @PathVariable String id){
        districtsService.deleteProvince(province_id, id);
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Xóa quận/huyện thành công")
                .result("Xóa thành công quận/huyện có mã " + id)
                .build();
    }

    @DeleteMapping("/delete/list-district/{province_id}")
    public ApiResponse<String> deleteListDistrict(@PathVariable String province_id){
        districtsService.deleteListProvinces(province_id);
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Xóa danh sách quận/huyện thành công")
                .result("Xóa danh sách quận/huyện thành công")
                .build();
    }

    @PutMapping("/update/{province_id}/{id}")
    public ApiResponse<DistrictsResponse> updateProvince(@PathVariable String province_id, @PathVariable String id, @RequestBody DistrictsRequest request){
        DistrictsResponse result = districtsService.updateDistricts(province_id, id, request);
        return ApiResponse.<DistrictsResponse>builder()
                .code(1000)
                .message("Cập nhật tỉnh/thành thành công")
                .result(result)
                .build();
    }
}
