package com.example.LuanVanTotNghiep.mapper;

import com.example.LuanVanTotNghiep.dto.request.Programs_Branchs_SchedulesRequest;

import com.example.LuanVanTotNghiep.dto.response.Programs_Branchs_SchedulesResponse;
import com.example.LuanVanTotNghiep.entity.Programs_Branchs_Schedules;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface Programs_Branchs_SchedulesMapper {
//    @Mapping(target = "program", ignore = true)
//    @Mapping(target = "branch", ignore = true)
//    @Mapping(target = "schedule", ignore = true)
    Programs_Branchs_Schedules toCreateProgramsBranchsSchedules(Programs_Branchs_SchedulesRequest request);

    @Mapping(target = "branchsEntityResponse",source = "branch")
    @Mapping(target = "schedulesResponse",source = "schedule")
    @Mapping(target = "programsResponse",source = "program")

    Programs_Branchs_SchedulesResponse toProgramsBranchsSchedulesResponse(Programs_Branchs_Schedules programsBranchsSchedules);
    void updateProgramsBranchsSchedules(@MappingTarget Programs_Branchs_Schedules programsBranchsSchedules, Programs_Branchs_SchedulesRequest request );

    @Mapping(target = "branchsEntityResponse",source = "branch")
    @Mapping(target = "schedulesResponse",source = "schedule")
    @Mapping(target = "programsResponse",source = "program")
    List<Programs_Branchs_SchedulesResponse> listProgramsBranchsSchedulesResponse(List<Programs_Branchs_Schedules> list);
    List<Programs_Branchs_Schedules> listProgramsBranchsSchedules(List<Programs_Branchs_SchedulesRequest> list);
}
