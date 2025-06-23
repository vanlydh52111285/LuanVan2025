package com.example.LuanVanTotNghiep.mapper;

import com.example.LuanVanTotNghiep.dto.request.SchedulesRequest;
import com.example.LuanVanTotNghiep.dto.response.SchedulesResponse;
import com.example.LuanVanTotNghiep.entity.Schedules;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SchedulesMapper {
    Schedules tocreateSchedule(SchedulesRequest request);
    SchedulesResponse toScheduleResponse(Schedules schedules);
    void updateSchedule(@MappingTarget Schedules schedules, SchedulesRequest request);
    List<SchedulesResponse> listSchedulesResponse(List<Schedules> list);
}
