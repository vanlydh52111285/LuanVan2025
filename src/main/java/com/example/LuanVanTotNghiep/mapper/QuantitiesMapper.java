package com.example.LuanVanTotNghiep.mapper;

import com.example.LuanVanTotNghiep.dto.request.QuantitiesRequest;
import com.example.LuanVanTotNghiep.dto.response.QuantitiesResponse;
import com.example.LuanVanTotNghiep.entity.Quantities;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuantitiesMapper {
    Quantities toCreateQuantity(QuantitiesRequest request);
    QuantitiesResponse toQuantityResponse(Quantities quantities);
    void updateQuantity(@MappingTarget Quantities quantities, QuantitiesRequest request);
    List<QuantitiesResponse> listQuantities(List<Quantities> list);
}
