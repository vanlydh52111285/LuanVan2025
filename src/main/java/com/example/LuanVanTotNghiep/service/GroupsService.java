package com.example.LuanVanTotNghiep.service;

import com.example.LuanVanTotNghiep.dto.request.GroupsRequest;
import com.example.LuanVanTotNghiep.dto.request.SubjectsRequest;
import com.example.LuanVanTotNghiep.dto.response.GroupsResponse;
import com.example.LuanVanTotNghiep.dto.response.SubjectResponse;
import com.example.LuanVanTotNghiep.entity.Groups;
import com.example.LuanVanTotNghiep.entity.Subjects;
import com.example.LuanVanTotNghiep.exception.AppException;
import com.example.LuanVanTotNghiep.exception.ErrorCode;
import com.example.LuanVanTotNghiep.mapper.GroupsMapper;
import com.example.LuanVanTotNghiep.repository.GroupsRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class GroupsService {
    GroupsRepository groupsRepository;
    GroupsMapper groupsMapper;
    ConvertService convertService;

    @Transactional
    public List<GroupsResponse> importGroupsFromExcel(MultipartFile file) {
        if (file.isEmpty()) {
            throw new AppException(ErrorCode.REQUEST_IS_EMPTY);
        }

        List<Groups> groupsList = new ArrayList<>();
        Set<String> existingIds = new HashSet<>(groupsRepository.findAll().stream().map(Groups::getGroup_id).toList());
        Set<String> duplicateIds = new HashSet<>();
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // Lấy sheet đầu tiên (Sheet1)
            Iterator<Row> rowIterator = sheet.iterator();

            // Bỏ qua header
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getCell(0) == null || row.getCell(1) == null) {
                    continue; // Bỏ qua dòng trống
                }

                String group_id = row.getCell(1).getStringCellValue().trim();
                String group_name = row.getCell(2).getStringCellValue().trim();
                String sub1 = row.getCell(3).getStringCellValue().trim();
                String sub2 =row.getCell(4).getStringCellValue().trim();
                String sub3 = row.getCell(5).getStringCellValue().trim();
                boolean status = convertService.convertToBoolean(row.getCell(6));


                if (existingIds.contains(group_id)) {
                    duplicateIds.add(group_name);
                    continue; // Bỏ qua mã vùng đã tồn tại
                }

                GroupsRequest request =  GroupsRequest.builder()
                        .group_id(group_id)
                        .groupname(group_name)
                        .sub1(sub1)
                        .sub2(sub2)
                        .sub3(sub3)
                        .status(status)
                        .build();

                Groups group = groupsMapper.toCreateGroups(request);
                groupsList.add(group);
            }

            if (!duplicateIds.isEmpty()) {
                throw new AppException(ErrorCode.GROUP_EXISTS, "Các tổ hợp môn đã tồn tại: " + String.join(", ", duplicateIds));
            }

            List<Groups> saveGroups = new ArrayList<>(groupsRepository.saveAll(groupsList));
            return saveGroups.stream().map(groupsMapper::toGroupsResponse).collect(Collectors.toList());
        } catch (IOException e) {
            throw new AppException(ErrorCode.UNEXPECTED_ERROR);
        }
    }

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
        return groupsMapper.listGroups(groupsRepository.findGroupsByStatusTrue());
    }
//    public void typegroup(String id, boolean type){
//        Groups groups = groupsRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.CONTENT_NO_EXISTS));
//        if ()
//    }
}
