package com.example.LuanVanTotNghiep.mapper;

import com.example.LuanVanTotNghiep.dto.request.BranchsRequest;
import com.example.LuanVanTotNghiep.dto.response.BranchsResponse;
import com.example.LuanVanTotNghiep.dto.response.Branchs_Entity_Response;
import com.example.LuanVanTotNghiep.dto.response.Branchs_GroupsResponse;
import com.example.LuanVanTotNghiep.dto.response.Branchs_ProgramsResponse;
import com.example.LuanVanTotNghiep.entity.Branchs;
import com.example.LuanVanTotNghiep.entity.Programs;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface BranchsMapper {
    @Mapping(target = "status",source = "status")
    Branchs toCreateBranch(BranchsRequest request);
    @Mapping(target = "status",source = "status")
    BranchsResponse toBranchResponse(Branchs branchs);
    @Mapping(target = "status",source = "status")
    void updateBranch(@MappingTarget Branchs branchs, BranchsRequest request );
    @Mapping(target = "status",source = "status")
    List<BranchsResponse> listBranchs(List<Branchs> List);
    @Mapping(target = "status",source = "status")
    List<Branchs_Entity_Response> lBranchsEntityResponses(List<Branchs> list);
    @Mapping(target = "status",source = "status")
    Branchs_GroupsResponse toBranchsGroupsResponse(Branchs branchs);
    @Mapping(target = "status",source = "status")
    Branchs_ProgramsResponse toBranchsProgramsResponse(Branchs updatedBranch);
    @Mapping(target = "status",source = "status")
    List<Branchs_ProgramsResponse> lBranchsProgramsResponses(List<Branchs> branchsList);
}
