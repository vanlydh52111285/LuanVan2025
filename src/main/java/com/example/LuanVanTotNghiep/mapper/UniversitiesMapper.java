package com.example.LuanVanTotNghiep.mapper;

import com.example.LuanVanTotNghiep.dto.request.UniversitiesRequest;
import com.example.LuanVanTotNghiep.dto.response.UniversitiesResponse;
import com.example.LuanVanTotNghiep.entity.Universities;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UniversitiesMapper {
    Universities toCreateUniversity(UniversitiesRequest request);
    UniversitiesResponse toUniversityResponse(Universities universities);
    void updateUniversity(@MappingTarget Universities universities, UniversitiesRequest request );
}
