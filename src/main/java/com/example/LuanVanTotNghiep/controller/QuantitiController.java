package com.example.LuanVanTotNghiep.controller;

import com.example.LuanVanTotNghiep.dto.request.QuantitiesRequest;
import com.example.LuanVanTotNghiep.dto.response.ApiResponse;
import com.example.LuanVanTotNghiep.dto.response.QuantitiesResponse;
import com.example.LuanVanTotNghiep.service.QuantitiesService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class QuantitiController {
    @Autowired
    private QuantitiesService quantitiesService;
    @PostMapping("/quantity")
    ApiResponse<QuantitiesResponse> createQuantity(@RequestBody @Valid QuantitiesRequest request){
        ApiResponse<QuantitiesResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(quantitiesService.createQuantity(request));
        return apiResponse;
    }
    @GetMapping("/quantity")
    ApiResponse<List<QuantitiesResponse>> getAllQuantities(){
        ApiResponse<List<QuantitiesResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(quantitiesService.getAllQuantities());
        return apiResponse;
    }
}
