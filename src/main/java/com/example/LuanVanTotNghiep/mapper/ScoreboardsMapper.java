package com.example.LuanVanTotNghiep.mapper;

import com.example.LuanVanTotNghiep.dto.request.ScoreboardsRequest;
import com.example.LuanVanTotNghiep.dto.response.ScoreboardsResponse;
import com.example.LuanVanTotNghiep.entity.Scoreboards;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScoreboardsMapper {
    Scoreboards toCreateScoreboards(ScoreboardsRequest request);
    ScoreboardsResponse toScoreboardsResponse(Scoreboards scoreboards);
    void updateScoreboards(@MappingTarget Scoreboards scoreboards, ScoreboardsRequest request);
    List<ScoreboardsResponse> listScoreboards(List<Scoreboards> scoreboards);
}
