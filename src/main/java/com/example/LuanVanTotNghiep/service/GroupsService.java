package com.example.LuanVanTotNghiep.service;

import com.example.LuanVanTotNghiep.dto.request.GroupsRequest;
import com.example.LuanVanTotNghiep.dto.response.GroupsResponse;
import com.example.LuanVanTotNghiep.entity.Groups;
import com.example.LuanVanTotNghiep.exception.AppException;
import com.example.LuanVanTotNghiep.exception.ErrorCode;
import com.example.LuanVanTotNghiep.mapper.GroupsMapper;
import com.example.LuanVanTotNghiep.repository.GroupsRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class GroupsService {
    GroupsRepository groupsRepository;
    GroupsMapper groupsMapper;
    public GroupsResponse createGroup(GroupsRequest request){
        if(groupsRepository.existsByContent(request.getContent())){
            throw  new AppException(ErrorCode.CONTENT_EXISTS);
        }
        Groups groups = groupsMapper.toCreateGroups(request);
        return groupsMapper.toGroupsResponse(groupsRepository.save(groups));
    }
    public List<GroupsResponse> getAllGroups(){
        List<GroupsResponse> groupsList = groupsMapper.listGroups(groupsRepository.findAll());
        return groupsList;
    }
    public void deleteGroups(String id){
        groupsRepository.deleteById(id);
    }
    public GroupsResponse updateGroups(String id, GroupsRequest request){
        Groups groups = groupsRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.CONTENT_NO_EXISTS));
        groupsMapper.updateGroups(groups,request);
        return groupsMapper.toGroupsResponse(groupsRepository.save(groups));
    }
}
