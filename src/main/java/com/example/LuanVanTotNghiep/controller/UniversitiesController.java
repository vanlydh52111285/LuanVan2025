package com.example.LuanVanTotNghiep.controller;

import com.example.LuanVanTotNghiep.dto.request.UniversitiesRequest;
import com.example.LuanVanTotNghiep.dto.response.ApiResponse;
import com.example.LuanVanTotNghiep.dto.response.UniversitiesResponse;
import com.example.LuanVanTotNghiep.service.UniversitiesService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class UniversitiesController {
    @Autowired
    private UniversitiesService universitiesService;
    @PostMapping("/university")
    ApiResponse<UniversitiesResponse> createUniversity(@RequestBody @Valid UniversitiesRequest request){
        ApiResponse<UniversitiesResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(universitiesService.createUniversity(request));
        return apiResponse;
    }
    @GetMapping("/university")
    ApiResponse<List<UniversitiesResponse>> getUniversities(){
        ApiResponse<List<UniversitiesResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(universitiesService.getUniversity());
        return apiResponse;
    }
}
