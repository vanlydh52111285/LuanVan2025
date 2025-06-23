package com.example.LuanVanTotNghiep.mapper;

import com.example.LuanVanTotNghiep.dto.request.GroupsRequest;

import com.example.LuanVanTotNghiep.dto.response.GroupsResponse;
import com.example.LuanVanTotNghiep.entity.Groups;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupsMapper {
    @Mapping(target = "type",source = "type")
    Groups toCreateGroups(GroupsRequest request);
    @Mapping(target = "type",source = "type")
    GroupsResponse toGroupsResponse(Groups groups);
    @Mapping(target = "type",source = "type")
    void updateGroups(@MappingTarget Groups groups, GroupsRequest request );
    @Mapping(target = "type",source = "type")
    List<GroupsResponse> listGroups(List<Groups> groupsList);
}
