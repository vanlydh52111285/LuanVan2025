package com.example.LuanVanTotNghiep.mapper;

import com.example.LuanVanTotNghiep.dto.request.BranchsRequest;
import com.example.LuanVanTotNghiep.dto.request.UsersRequest;
import com.example.LuanVanTotNghiep.dto.response.BranchsResponse;
import com.example.LuanVanTotNghiep.dto.response.Branchs_GroupsResponse;
import com.example.LuanVanTotNghiep.dto.response.Branchs_QuantitiesResponse;
import com.example.LuanVanTotNghiep.dto.response.UsersResponse;
import com.example.LuanVanTotNghiep.entity.Branchs;
import com.example.LuanVanTotNghiep.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BranchsMapper {
    Branchs toCreateBranch(BranchsRequest request);
    BranchsResponse toBranchResponse(Branchs branchs);
    void updateBrach(@MappingTarget Branchs branchs, BranchsRequest request );
    List<BranchsResponse> listBranchs(List<Branchs> List);
    Branchs_GroupsResponse toBranchsGroupsResponse(Branchs branchs);
    Branchs_QuantitiesResponse tBranchsQuantitiesResponse(Branchs branchs);
}
