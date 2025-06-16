package com.example.LuanVanTotNghiep.mapper;


import com.example.LuanVanTotNghiep.dto.request.BranchsRequest;
import com.example.LuanVanTotNghiep.dto.request.ProgramsRequest;
import com.example.LuanVanTotNghiep.dto.response.ProgramsResponse;
import com.example.LuanVanTotNghiep.entity.Programs;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProgramsMapper {
    Programs toCreateProgram(ProgramsRequest request);
    ProgramsResponse toProgramResponse(Programs programs);
    void updateProgram(@MappingTarget Programs programs, ProgramsRequest request );
    List<ProgramsResponse> listPrograms(List<Programs> List);
}
