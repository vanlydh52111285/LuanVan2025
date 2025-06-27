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
//    @Mapping(target = "status",source = "status")
    Groups toCreateGroups(GroupsRequest request);
//    @Mapping(target = "status",source = "status")
    GroupsResponse toGroupsResponse(Groups groups);
//    @Mapping(target = "status",source = "status")
    void updateGroups(@MappingTarget Groups groups, GroupsRequest request );
//    @Mapping(target = "status",source = "status")
    List<GroupsResponse> listGroups(List<Groups> groupsList);
}
