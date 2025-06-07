package com.example.LuanVanTotNghiep.controller;

import com.example.LuanVanTotNghiep.dto.request.UsersRequest;
import com.example.LuanVanTotNghiep.dto.response.ApiResponse;
import com.example.LuanVanTotNghiep.dto.response.UsersResponse;
import com.example.LuanVanTotNghiep.service.UsersService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UsersController {
    @Autowired
    private UsersService usersService;
    @PostMapping("/student/register")
    ApiResponse<UsersResponse> register (@RequestBody @Valid UsersRequest request){
        ApiResponse<UsersResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(usersService.createStudent(request));
        return apiResponse;
    }
}
