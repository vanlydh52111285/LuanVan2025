package com.example.LuanVanTotNghiep.controller;

import com.example.LuanVanTotNghiep.dto.request.ScoreboardsRequest;
import com.example.LuanVanTotNghiep.dto.response.ApiResponse;
import com.example.LuanVanTotNghiep.dto.response.ScoreboardsResponse;
import com.example.LuanVanTotNghiep.service.ScoreboardsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scoreboards")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScoreboardsController {

    ScoreboardsService scoreboardsService;

    @GetMapping("/get-by-application/{application_id}")
    public ApiResponse<ScoreboardsResponse> getScoreboardByApplicationId(@PathVariable String application_id) {
        ScoreboardsResponse result = scoreboardsService.getScoreboardByApplicationId(application_id);
        return ApiResponse.<ScoreboardsResponse>builder()
                .code(1000)
                .message("Lấy bảng điểm theo application_id thành công")
                .result(result)
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<ScoreboardsResponse> createScoreboard(@RequestBody ScoreboardsRequest request) {
        ScoreboardsResponse result = scoreboardsService.createScoreboard(request);
        return ApiResponse.<ScoreboardsResponse>builder()
                .code(1000)
                .message("Tạo bảng điểm thành công")
                .result(result)
                .build();
    }

}
