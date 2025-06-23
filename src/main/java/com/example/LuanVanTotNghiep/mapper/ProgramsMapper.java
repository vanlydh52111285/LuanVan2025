package com.example.LuanVanTotNghiep.mapper;


import com.example.LuanVanTotNghiep.dto.request.BranchsRequest;
import com.example.LuanVanTotNghiep.dto.request.ProgramsRequest;
import com.example.LuanVanTotNghiep.dto.response.ProgramsResponse;
import com.example.LuanVanTotNghiep.entity.Programs;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProgramsMapper {
    @Mapping(target = "type",source = "type")
    Programs toCreateProgram(ProgramsRequest request);
    @Mapping(target = "type",source = "type")
    ProgramsResponse toProgramResponse(Programs programs);
    @Mapping(target = "type",source = "type")
    void updateProgram(@MappingTarget Programs programs, ProgramsRequest request );
    @Mapping(target = "type",source = "type")
    List<ProgramsResponse> listPrograms(List<Programs> List);
}
