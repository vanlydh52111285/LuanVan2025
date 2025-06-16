package com.example.LuanVanTotNghiep.controller;

import com.example.LuanVanTotNghiep.dto.request.ProgramsRequest;
import com.example.LuanVanTotNghiep.dto.response.ApiResponse;
import com.example.LuanVanTotNghiep.dto.response.ProgramsResponse;
import com.example.LuanVanTotNghiep.service.ProgramsService;
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
public class ProgramsController {
    @Autowired
    private ProgramsService programsService;
    @PostMapping("/program")
    ApiResponse<ProgramsResponse> createProgram(@RequestBody @Valid ProgramsRequest request){
        ApiResponse<ProgramsResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(programsService.createProgram(request));
        return apiResponse;
    }
    @GetMapping("/program")
    ApiResponse<List<ProgramsResponse>> getAllPrograms(){
        ApiResponse<List<ProgramsResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(programsService.getAllPrograms());
        return apiResponse;
    }
}
