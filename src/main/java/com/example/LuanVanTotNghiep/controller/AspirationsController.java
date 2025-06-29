package com.example.LuanVanTotNghiep.controller;

import com.example.LuanVanTotNghiep.dto.request.AspirationsRequest;
import com.example.LuanVanTotNghiep.dto.response.ApiResponse;
import com.example.LuanVanTotNghiep.dto.response.AspirationsResponse;
import com.example.LuanVanTotNghiep.service.AspirationsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aspirations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AspirationsController {

    AspirationsService aspirationsService;

    @PostMapping("/create")
    public ApiResponse<AspirationsResponse> createAspirations(@RequestBody AspirationsRequest request) {
        AspirationsResponse result = aspirationsService.createAspirations(request);
        return ApiResponse.<AspirationsResponse>builder()
                .code(1000)
                .message("Tạo nguyện vọng thành công")
                .result(result)
                .build();
    }
}
