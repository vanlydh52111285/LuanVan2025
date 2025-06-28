package com.example.LuanVanTotNghiep.service;

import com.example.LuanVanTotNghiep.dto.request.ScoreboardSubjectRequest;
import com.example.LuanVanTotNghiep.dto.request.ScoreboardsRequest;
import com.example.LuanVanTotNghiep.dto.response.ScoreboardsResponse;
import com.example.LuanVanTotNghiep.entity.Applications;
import com.example.LuanVanTotNghiep.entity.Scoreboards;
import com.example.LuanVanTotNghiep.exception.AppException;
import com.example.LuanVanTotNghiep.exception.ErrorCode;
import com.example.LuanVanTotNghiep.mapper.ScoreboardsMapper;
import com.example.LuanVanTotNghiep.repository.ApplicationsRepository;
import com.example.LuanVanTotNghiep.repository.ScoreboardsRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScoreboardsService {
    ScoreboardsRepository scoreboardsRepository;
    ScoreboardsMapper scoreboardsMapper;
    ScoreboardSubjectService scoreboardSubjectService;
    ApplicationsRepository applicationsRepository;

    @Transactional
    public ScoreboardsResponse getScoreboardByApplicationId(String applicationId) {
        // Kiểm tra tồn tại của Applications
        Applications application = applicationsRepository.findById(applicationId)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND, "Không tìm thấy đơn xét tuyển với mã: " + applicationId));

        // Lấy Scoreboards liên kết với application
        Scoreboards scoreboard = scoreboardsRepository.findByApplicationId(application.getApplication_id())
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND, "Không tìm thấy bảng điểm cho đơn xét tuyển: " + applicationId));

        return scoreboardsMapper.toScoreboardsResponse(scoreboard);
    }

    @Transactional
    public ScoreboardsResponse createScoreboard(ScoreboardsRequest request) {
        // Kiểm tra tồn tại của Applications
        Applications application = applicationsRepository.findById(request.getApplication_id())
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND, "Không tìm thấy đơn xét tuyển với mã: " + request.getApplication_id()));

        // Tạo và lưu Scoreboards
        Scoreboards scoreboard = scoreboardsMapper.toCreateScoreboards(request);
        scoreboard.setApplications(application);
        Scoreboards savedScoreboard = scoreboardsRepository.save(scoreboard);

        // Gọi ScoreboardSubjectService để thêm các bản ghi trung gian
        request.getSubjects().forEach(subjectRequest -> {
            scoreboardSubjectService.createScoreboardSubject(ScoreboardSubjectRequest.builder()
                    .score_id(savedScoreboard.getScore_id())
                    .sub_id(subjectRequest.getSub_id())
                    .average_score(subjectRequest.getAverage_score())
                    .build());
        });

        return scoreboardsMapper.toScoreboardsResponse(savedScoreboard);
    }
}
