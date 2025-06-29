package com.example.LuanVanTotNghiep.mapper;

import com.example.LuanVanTotNghiep.dto.request.AspirationsRequest;
import com.example.LuanVanTotNghiep.dto.response.AspirationsResponse;
import com.example.LuanVanTotNghiep.entity.Aspirations;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AspirationsMapper {
    Aspirations toCreateAspirations(AspirationsRequest request);
    AspirationsResponse toAspirationsResponse(Aspirations aspirations);
    void updateAspirations(@MappingTarget Aspirations aspirations, AspirationsRequest request);
    List<AspirationsResponse> lisAspirations(List<Aspirations> aspirations);
}
