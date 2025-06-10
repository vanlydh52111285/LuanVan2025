package com.example.LuanVanTotNghiep.mapper;

import com.example.LuanVanTotNghiep.dto.request.ApplicationsRequest;
import com.example.LuanVanTotNghiep.dto.response.ApplicationsResponse;
import com.example.LuanVanTotNghiep.entity.Applications;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ApplicationsMapper {
    @Mapping(target = "application_id", ignore = true)
    @Mapping(target = "create_date", ignore = true)
    @Mapping(target = "update_date", ignore = true)
    @Mapping(target = "application_type", source = "applicationType")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "total_score", source = "totalScore")
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "admission_method", expression = "java(request.getMethodId() != null ? com.example.LuanVanTotNghiep.entity.Methods.builder().method_id(request.getMethodId()).build() : null)")
    @Mapping(target = "documentsList", ignore = true)
    Applications toCreateApplications(ApplicationsRequest request);

    @Mapping(source = "application_id", target = "applicationId")
    @Mapping(source = "application_type", target = "applicationType")
    @Mapping(source = "total_score", target = "totalScore")
    @Mapping(source = "create_date", target = "createDate")
    @Mapping(source = "update_date", target = "updateDate")
    @Mapping(source = "user.user_id", target = "userId")
    @Mapping(source = "admission_method.method_id", target = "methodId", defaultExpression = "java(null)")
    ApplicationsResponse toApplicationsResponse(Applications application);

    @Mapping(target = "application_id", ignore = true)
    @Mapping(target = "create_date", ignore = true)
    @Mapping(target = "update_date", ignore = true)
    @Mapping(target = "application_type", source = "applicationType")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "total_score", source = "totalScore")
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "admission_method", expression = "java(request.getMethodId() != null ? com.example.LuanVanTotNghiep.entity.Methods.builder().method_id(request.getMethodId()).build() : null)")
    @Mapping(target = "documentsList", ignore = true)
    void updateApplications(@MappingTarget Applications application, ApplicationsRequest request);

    List<ApplicationsResponse> listApplications(List<Applications> applications);
}