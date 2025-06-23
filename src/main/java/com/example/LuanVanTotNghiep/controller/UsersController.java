package com.example.LuanVanTotNghiep.controller;

import com.example.LuanVanTotNghiep.dto.request.UsersRequest;
import com.example.LuanVanTotNghiep.dto.response.ApiResponse;
import com.example.LuanVanTotNghiep.dto.response.UsersResponse;
import com.example.LuanVanTotNghiep.service.AuthenticationService;
import com.example.LuanVanTotNghiep.service.UsersService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UsersController {
    UsersService usersService;
    AuthenticationService authenticationService;

    @PostMapping("/student/register")
    ApiResponse<UsersResponse> register (@RequestBody @Valid UsersRequest request){
        ApiResponse<UsersResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(usersService.createStudent(request));
        return apiResponse;
    }
    @PostMapping("/admin/create-cadre")
    ApiResponse<UsersResponse> createCadre (@RequestBody @Valid UsersRequest request){
        ApiResponse<UsersResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(usersService.createCadre(request));
        return apiResponse;
    }
    @DeleteMapping("/admin/users/{id}")
    ApiResponse<String> deleteUser (@PathVariable String id){
        usersService.deleteUsers(id);
        ApiResponse<String> apiResponse =new ApiResponse<>();
        apiResponse.setResult("success");
        return apiResponse;
    }
    @PutMapping("/student/update-users")
    ApiResponse<UsersResponse> updateUsers(@PathVariable String id , @RequestBody UsersRequest request){
        ApiResponse<UsersResponse> apiResponse =  new ApiResponse<>();
        apiResponse.setResult(usersService.updateUsers(id,request));
        return apiResponse;
    }
    @GetMapping("/admin/get-all-users")
    ApiResponse<List<UsersResponse>> getAllUsers(){
        ApiResponse<List<UsersResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(usersService.getAllUsers());
        return apiResponse;
    }

    @GetMapping("/user/get-user-profile")
    ApiResponse<UsersResponse> getUserById() {
        String id = authenticationService.getAuthenticatedUserId();
        ApiResponse<UsersResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(usersService.getUsers(id));
        return apiResponse;
    }
}
