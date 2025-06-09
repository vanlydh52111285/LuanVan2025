package com.example.LuanVanTotNghiep.controller;

import com.example.LuanVanTotNghiep.dto.request.GroupsRequest;
import com.example.LuanVanTotNghiep.dto.response.ApiResponse;
import com.example.LuanVanTotNghiep.dto.response.GroupsResponse;
import com.example.LuanVanTotNghiep.service.GroupsService;
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
public class GroupsController {
    @Autowired
    private GroupsService groupsService;
    @PostMapping("/groups")
    ApiResponse<GroupsResponse> createGroups(@RequestBody @Valid GroupsRequest request){
        ApiResponse<GroupsResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(groupsService.createGroup(request));
        return apiResponse;
    }
    @GetMapping("/groups")
    ApiResponse<List<GroupsResponse>> getAllGroups(){
        ApiResponse<List<GroupsResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(groupsService.getAllGroups());
        return apiResponse;
    }
}
