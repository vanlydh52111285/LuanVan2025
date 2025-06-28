package com.example.LuanVanTotNghiep.mapper;

import com.example.LuanVanTotNghiep.dto.request.ScoreboardSubjectRequest;
import com.example.LuanVanTotNghiep.dto.response.ScoreboardSubjectResponse;
import com.example.LuanVanTotNghiep.entity.ScoreboardSubject;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScoreboardSubjectMapper {
    ScoreboardSubject toCreateScoreboardSubject(ScoreboardSubjectRequest request);
    ScoreboardSubjectResponse toScoreboardSubjectResponse(ScoreboardSubject scoreboardSubject);
    void updateScoreboardSubject(@MappingTarget ScoreboardSubject scoreboardSubject, ScoreboardSubjectRequest request);
    List<ScoreboardSubjectResponse> listScoreboardSubject(List<ScoreboardSubject> scoreboardSubject);
}
