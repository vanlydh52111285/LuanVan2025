package com.example.LuanVanTotNghiep.mapper;

import com.example.LuanVanTotNghiep.dto.request.MethodsRequest;
import com.example.LuanVanTotNghiep.dto.response.MethodResponse;
import com.example.LuanVanTotNghiep.entity.Methods;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MethodsMapper {
    Methods toCreateMethod(MethodsRequest request);
    MethodResponse toMethodResponse(Methods methods);

    // Cho cập nhật
    @Mapping(target = "method_id", ignore = true) // Bỏ qua method_id
    void updateMethod(@MappingTarget Methods methods, MethodsRequest request );
    List<MethodResponse> listMethod(List<Methods> methodsList);
}
