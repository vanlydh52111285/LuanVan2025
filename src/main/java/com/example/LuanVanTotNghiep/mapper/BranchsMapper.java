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
    @Mapping(target = "type",source = "type")
    Branchs toCreateBranch(BranchsRequest request);
    @Mapping(target = "type",source = "type")
    BranchsResponse toBranchResponse(Branchs branchs);
    @Mapping(target = "type",source = "type")
    void updateBrach(@MappingTarget Branchs branchs, BranchsRequest request );
    @Mapping(target = "type",source = "type")
    List<BranchsResponse> listBranchs(List<Branchs> List);
    @Mapping(target = "type",source = "type")
    List<Branchs_Entity_Response> lBranchsEntityResponses(List<Branchs> list);
    @Mapping(target = "type",source = "type")
    Branchs_GroupsResponse toBranchsGroupsResponse(Branchs branchs);
    @Mapping(target = "type",source = "type")
    Branchs_ProgramsResponse toBranchsProgramsResponse(Branchs updatedBranch);
    @Mapping(target = "type",source = "type")
    List<Branchs_ProgramsResponse> lBranchsProgramsResponses(List<Branchs> branchsList);
}
