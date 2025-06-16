package com.example.LuanVanTotNghiep.mapper;

import com.example.LuanVanTotNghiep.dto.request.ApplicationsRequest;
import com.example.LuanVanTotNghiep.dto.response.ApplicationsResponse;
import com.example.LuanVanTotNghiep.entity.Applications;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ApplicationsMapper {
    Applications toCreateApplications(ApplicationsRequest request);
    ApplicationsResponse toApplicationsResponse(Applications application);
    void updateApplications(@MappingTarget Applications application, ApplicationsRequest request);
    List<ApplicationsResponse> listApplications(List<Applications> applications);
}