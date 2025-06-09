package com.example.LuanVanTotNghiep.mapper;

import com.example.LuanVanTotNghiep.dto.request.GroupsRequest;

import com.example.LuanVanTotNghiep.dto.response.GroupsResponse;
import com.example.LuanVanTotNghiep.dto.response.UsersResponse;
import com.example.LuanVanTotNghiep.entity.Groups;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupsMapper {
    Groups toCreateGroups(GroupsRequest request);
    GroupsResponse toGroupsResponse(Groups groups);
    void updateGroups(@MappingTarget Groups groups, GroupsRequest request );
    List<GroupsResponse> listGroups(List<Groups> groupsList);
}
