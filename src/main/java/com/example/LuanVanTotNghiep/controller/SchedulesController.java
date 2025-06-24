package com.example.LuanVanTotNghiep.controller;

import com.example.LuanVanTotNghiep.dto.request.Programs_Branchs_SchedulesRequest;
import com.example.LuanVanTotNghiep.dto.request.SchedulesRequest;
import com.example.LuanVanTotNghiep.dto.response.ApiResponse;
import com.example.LuanVanTotNghiep.dto.response.Programs_Branchs_SchedulesResponse;
import com.example.LuanVanTotNghiep.dto.response.SchedulesResponse;
import com.example.LuanVanTotNghiep.service.SchedulesService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class SchedulesController {
    @Autowired
    private SchedulesService schedulesService;
    @PostMapping("/schedule")
    ApiResponse<SchedulesResponse> createSchedule(@RequestBody @Valid SchedulesRequest request){
        ApiResponse<SchedulesResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(schedulesService.createSchedule(request));
        return apiResponse;
    }
    @GetMapping("/schedules")
    ApiResponse<List<SchedulesResponse>> getAllSchedules(){
        ApiResponse<List<SchedulesResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(schedulesService.getAllSchedules());
        return apiResponse;
    }
    @PostMapping("/create-list-pbs")
    ApiResponse<List<Programs_Branchs_SchedulesResponse>> createListPBS(@RequestBody @Valid List<Programs_Branchs_SchedulesRequest> requests){
        ApiResponse<List<Programs_Branchs_SchedulesResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(schedulesService.createListPBS(requests));
        return apiResponse;
    }
    @GetMapping("/list-pbs/{schedule_id}")
    ApiResponse<List<Programs_Branchs_SchedulesResponse>> getPBS(@PathVariable String schedule_id){
        ApiResponse<List<Programs_Branchs_SchedulesResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(schedulesService.getProgramsBranchsSchedules(schedule_id));
        return apiResponse;
    }
}
