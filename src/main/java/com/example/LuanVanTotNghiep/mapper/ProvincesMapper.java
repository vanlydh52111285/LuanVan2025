package com.example.LuanVanTotNghiep.mapper;

import com.example.LuanVanTotNghiep.dto.request.ProvincesRequest;
import com.example.LuanVanTotNghiep.dto.response.ProgramsResponse;
import com.example.LuanVanTotNghiep.dto.response.ProvincesResponse;
import com.example.LuanVanTotNghiep.entity.Provinces;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProvincesMapper {
    Provinces toCreateProvinces(ProvincesRequest request);
    ProvincesResponse toProvinceResponse(Provinces provinces);
    void updateProvince(@MappingTarget Provinces provinces, ProvincesRequest request );
    List<ProvincesResponse> listProvinces(List<Provinces> provincesList);
}
