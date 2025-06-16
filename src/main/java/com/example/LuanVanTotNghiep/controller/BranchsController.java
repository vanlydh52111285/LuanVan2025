package com.example.LuanVanTotNghiep.controller;

import com.example.LuanVanTotNghiep.dto.request.BranchsRequest;
import com.example.LuanVanTotNghiep.dto.request.Branchs_GroupsRequest;
import com.example.LuanVanTotNghiep.dto.response.ApiResponse;
import com.example.LuanVanTotNghiep.dto.response.BranchsResponse;
import com.example.LuanVanTotNghiep.dto.response.Branchs_GroupsResponse;
import com.example.LuanVanTotNghiep.service.BranchsService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class BranchsController {
    @Autowired
    private BranchsService branchsService;
    @PostMapping("/branch")
    ApiResponse<BranchsResponse> createBarchs(@RequestBody @Valid BranchsRequest request){
        ApiResponse<BranchsResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(branchsService.createBranch(request));
        return apiResponse;
    }
    @GetMapping("/branch")
    ApiResponse<List<BranchsResponse>> getAllBranchs(){
        ApiResponse<List<BranchsResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(branchsService.getAllBranchs());
        return apiResponse;
    }
    @PostMapping("/create-branch-group")
    ApiResponse<Branchs_GroupsResponse> createBranchs_Groups(@RequestBody @Valid Branchs_GroupsRequest request){
        ApiResponse<Branchs_GroupsResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(branchsService.createBranchs_Groups(request));
        return apiResponse;
    }
    @DeleteMapping("/delete-branch-group")
    ApiResponse<String> deleteBranchs_Groups(@RequestBody @Valid Branchs_GroupsRequest request){
        branchsService.deleteBranch_Groups(request);
        ApiResponse<String> apiResponse =new ApiResponse<>();
        apiResponse.setResult("success");
        return apiResponse;
    }
}
