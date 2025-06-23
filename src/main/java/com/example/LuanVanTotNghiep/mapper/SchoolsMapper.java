package com.example.LuanVanTotNghiep.mapper;

import com.example.LuanVanTotNghiep.dto.request.SchoolsRequest;
import com.example.LuanVanTotNghiep.dto.response.SchoolsResponse;
import com.example.LuanVanTotNghiep.entity.Schools;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SchoolsMapper {
    Schools toCreateSchools(SchoolsRequest request);
    SchoolsResponse toSchoolsResponse(Schools schools);
    void updateSchool(@MappingTarget Schools schools, SchoolsRequest request);
    List<SchoolsResponse> listSchools(List<Schools> schoolsList);
}
