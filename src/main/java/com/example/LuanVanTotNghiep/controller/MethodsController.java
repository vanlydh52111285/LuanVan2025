package com.example.LuanVanTotNghiep.controller;

import com.example.LuanVanTotNghiep.dto.request.MethodsRequest;
import com.example.LuanVanTotNghiep.dto.response.ApiResponse;
import com.example.LuanVanTotNghiep.dto.response.MethodResponse;
import com.example.LuanVanTotNghiep.service.MethodsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/methods")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MethodsController {

    @Autowired
    MethodsService methodsService;

    @PostMapping("/create")
    public ApiResponse<MethodResponse> createMethod(@RequestBody MethodsRequest request) {
        MethodResponse result = methodsService.createMethod(request);
        return ApiResponse.<MethodResponse>builder()
                .code(1000)
                .message("Tạo phương thức thành công")
                .result(result)
                .build();
    }

    @PutMapping("/update/{method_id}")
    public ApiResponse<MethodResponse> updateMethod(@PathVariable String method_id, @RequestBody MethodsRequest request) {
        MethodResponse result = methodsService.updateMethod(method_id, request);
        return ApiResponse.<MethodResponse>builder()
                .code(1000)
                .message("Cập nhật phương thức thành công")
                .result(result)
                .build();
    }

    @DeleteMapping("/delete/{method_id}")
    public ApiResponse<String> deleteMethod(@PathVariable String method_id) {
        methodsService.deleteMethod(method_id);
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Xóa phương thức thành công")
                .result("Method with ID " + method_id + " deleted successfully")
                .build();
    }

    @GetMapping("/getMethodById/{method_id}")
    public ApiResponse<MethodResponse> getMethodById(@PathVariable String method_id) {
        MethodResponse result = methodsService.getMethodById(method_id);
        return ApiResponse.<MethodResponse>builder()
                .code(1000)
                .message("Lấy phương thức thành công")
                .result(result)
                .build();
    }

    @GetMapping("/getAllMethods")
    public ApiResponse<List<MethodResponse>> getAllMethods() {
        List<MethodResponse> result = methodsService.getAllMethods();
        return ApiResponse.<List<MethodResponse>>builder()
                .code(1000)
                .message("Lấy danh sách phương thức thành công")
                .result(result)
                .build();
    }
}
