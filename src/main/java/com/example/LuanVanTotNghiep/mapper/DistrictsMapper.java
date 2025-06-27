package com.example.LuanVanTotNghiep.mapper;

import com.example.LuanVanTotNghiep.dto.request.DistrictsRequest;
import com.example.LuanVanTotNghiep.dto.response.DistrictsResponse;
import com.example.LuanVanTotNghiep.entity.Districts;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DistrictsMapper {
    Districts toCreateDistricts(DistrictsRequest request);
    DistrictsResponse toDistrictsResponse(Districts districts);
    void updateDistrict(@MappingTarget Districts districts, DistrictsRequest request);
    List<DistrictsResponse> listDistricts(List<Districts> districtsList);
}
