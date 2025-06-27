package com.example.LuanVanTotNghiep.controller;

import com.cloudinary.Api;
import com.example.LuanVanTotNghiep.dto.request.BranchsRequest;
import com.example.LuanVanTotNghiep.dto.request.Branchs_GroupsRequest;
import com.example.LuanVanTotNghiep.dto.request.Branchs_ProgramsRequest;
import com.example.LuanVanTotNghiep.dto.request.GroupsRequest;
import com.example.LuanVanTotNghiep.dto.response.*;
import com.example.LuanVanTotNghiep.service.BranchsService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
public class BranchsController {
    @Autowired
    private BranchsService branchsService;

    @GetMapping("/branch")
    ApiResponse<List<Branchs_Entity_Response>> getAllBranchs(){
        ApiResponse<List<Branchs_Entity_Response>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(branchsService.getAllBranchs());
        return apiResponse;
    }
    @PostMapping("/branch")
    ApiResponse<BranchsResponse> createBarchs(@RequestBody @Valid BranchsRequest request){
        ApiResponse<BranchsResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(branchsService.createBranch(request));
        return apiResponse;
    }

    @PostMapping("/branch/import")
    ApiResponse<List<BranchsResponse>> importBranchsFromExcel(@RequestParam("file") MultipartFile file){
        ApiResponse<List<BranchsResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(branchsService.importBranchsFromExcel(file));
        return apiResponse;
    }

    @DeleteMapping("/branch/delete/{id}")
    ApiResponse<String> deleteBranch(@PathVariable String id){
        branchsService.deleteBranch(id);
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult("success");
        return apiResponse;
    }
    @PutMapping("/branch/update/{id}")
    ApiResponse<BranchsResponse> updateBranch(@PathVariable String id,@RequestBody BranchsRequest request){
        ApiResponse<BranchsResponse> apiResponse =new ApiResponse<>();
        apiResponse.setResult(branchsService.updateBranch(id,request));
        return apiResponse;
    }

    @PostMapping("/create-branch-group")
    ApiResponse<Branchs_GroupsResponse> createBranchs_Groups(@RequestBody @Valid Branchs_GroupsRequest request){
        ApiResponse<Branchs_GroupsResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(branchsService.createBranchs_Groups(request));
        return apiResponse;
    }
//    @DeleteMapping("/delete-branch-group")
//    ApiResponse<String> deleteBranchs_Groups(@RequestBody @Valid Branchs_GroupsRequest request){
//        branchsService.deleteBranch_Groups(request);
//        ApiResponse<String> apiResponse =new ApiResponse<>();
//        apiResponse.setResult("success");
//        return apiResponse;
//    }
    @PostMapping("/create-branch-program")
    ApiResponse<Branchs_ProgramsResponse> createBranchs_Programs(@RequestBody @Valid Branchs_ProgramsRequest request){
        ApiResponse<Branchs_ProgramsResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(branchsService.createBranchProgram(request));
        return apiResponse;
    }
    @GetMapping("/groups-by-branch/{branch_id}")
    ApiResponse<List<GroupsResponse>> getGroupsByBranch(@PathVariable String branch_id){
        ApiResponse<List<GroupsResponse>>apiResponse = new ApiResponse<>();
        apiResponse.setResult(branchsService.getGroupsByBranch(branch_id));
        return apiResponse;
    }
    @GetMapping("/programs-by-branch/{branch_id}")
    ApiResponse<List<ProgramsResponse>> getProgramsByBranch(@PathVariable String branch_id){
        ApiResponse<List<ProgramsResponse>>apiResponse = new ApiResponse<>();
        apiResponse.setResult(branchsService.getProgramsByBranch(branch_id));
        return apiResponse;
    }
    @GetMapping("/branchs-by-program/{program_id}")
    ApiResponse<List<Branchs_Entity_Response>> getBranchsByProgram(@PathVariable String program_id){
        ApiResponse<List<Branchs_Entity_Response>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(branchsService.getBranchsByProgram(program_id));
        return apiResponse;
    }
    @GetMapping("/branch-by-group-program")
    ApiResponse<List<Branchs_ProgramsResponse>> getBranchsByGoupsAndPrograms(){
        ApiResponse<List<Branchs_ProgramsResponse>> apiResponse= new ApiResponse<>();
        apiResponse.setResult(branchsService.getBranchsByGroupsAndPrograms());
        return apiResponse;
    }
}
