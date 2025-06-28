package com.example.LuanVanTotNghiep.service;

import com.example.LuanVanTotNghiep.dto.request.ScoreboardSubjectRequest;
import com.example.LuanVanTotNghiep.entity.ScoreboardSubject;
import com.example.LuanVanTotNghiep.entity.Scoreboards;
import com.example.LuanVanTotNghiep.entity.Subjects;
import com.example.LuanVanTotNghiep.exception.AppException;
import com.example.LuanVanTotNghiep.exception.ErrorCode;
import com.example.LuanVanTotNghiep.mapper.ScoreboardSubjectMapper;
import com.example.LuanVanTotNghiep.repository.ScoreboardSubjectRepository;
import com.example.LuanVanTotNghiep.repository.ScoreboardsRepository;
import com.example.LuanVanTotNghiep.repository.SubjectsRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScoreboardSubjectService {
    ScoreboardSubjectRepository scoreboardSubjectRepository;
    ScoreboardsRepository scoreboardsRepository;
    SubjectsRepository subjectsRepository;
    ScoreboardSubjectMapper scoreboardSubjectMapper;

    @Transactional
    public void createScoreboardSubject(ScoreboardSubjectRequest request) {
        // Kiểm tra tồn tại của Scoreboards và Subjects
        Scoreboards scoreboard = scoreboardsRepository.findById(request.getScore_id())
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND, "Không tìm thấy bảng điểm"));
        Subjects subject = subjectsRepository.findById(request.getSub_id())
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND, "Không tìm thấy môn học"));

        // Tạo và lưu ScoreboardSubject
        ScoreboardSubject scoreboardSubject = scoreboardSubjectMapper.toCreateScoreboardSubject(request);
        scoreboardSubject.setScoreboard(scoreboard);
        scoreboardSubject.setSubject(subject);

        ScoreboardSubject savedScoreboardSubject = scoreboardSubjectRepository.save(scoreboardSubject);
        scoreboardSubjectMapper.toScoreboardSubjectResponse(savedScoreboardSubject);
    }
}
