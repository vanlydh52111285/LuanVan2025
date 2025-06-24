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
        if(groupsRepository.existsById(request.getGroup_id())){
            throw new AppException(ErrorCode.GROUP_EXISTS);
        }
        if(groupsRepository.existsByGroupname(request.getGroupname())){
            throw  new AppException(ErrorCode.CONTENT_EXISTS);
        }
        Groups groups = groupsMapper.toCreateGroups(request);
        //groups.setType(request.isType());
        return groupsMapper.toGroupsResponse(groupsRepository.save(groups));
    }
    public List<GroupsResponse> getAllGroups(){
        List<GroupsResponse> groupsList = groupsMapper.listGroups(groupsRepository.findAll());
        return groupsList;
    }
    public void deleteGroups(String id){
        if (groupsRepository.existsByGroupId(id)){
            throw new AppException(ErrorCode.CONSTRAINT_VIOLATION);
        }
        groupsRepository.deleteById(id);
    }
    public GroupsResponse updateGroups(String group_id, GroupsRequest request){
        Groups groups = groupsRepository.findById(group_id).orElseThrow(()->new AppException(ErrorCode.CONTENT_NO_EXISTS));
        if (groupsRepository.existsByGroupId(group_id)){
            throw new AppException(ErrorCode.CONSTRAINT_VIOLATION);
        }
        groupsMapper.updateGroups(groups,request);
        return groupsMapper.toGroupsResponse(groupsRepository.save(groups));
    }
    public List<GroupsResponse> getAllGroupsTrue(){
        return groupsMapper.listGroups(groupsRepository.findGroupsByTypeTrue());
    }
//    public void typegroup(String id, boolean type){
//        Groups groups = groupsRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.CONTENT_NO_EXISTS));
//        if ()
//    }
}
