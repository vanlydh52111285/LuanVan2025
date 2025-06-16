package com.example.LuanVanTotNghiep.service;

import com.example.LuanVanTotNghiep.dto.request.BranchsRequest;
import com.example.LuanVanTotNghiep.dto.request.Branchs_GroupsRequest;
import com.example.LuanVanTotNghiep.dto.response.BranchsResponse;
import com.example.LuanVanTotNghiep.dto.response.Branchs_GroupsResponse;
import com.example.LuanVanTotNghiep.dto.response.UniversitiesResponse;
import com.example.LuanVanTotNghiep.entity.Branchs;
import com.example.LuanVanTotNghiep.entity.Groups;
import com.example.LuanVanTotNghiep.entity.Universities;
import com.example.LuanVanTotNghiep.exception.AppException;
import com.example.LuanVanTotNghiep.exception.ErrorCode;
import com.example.LuanVanTotNghiep.mapper.BranchsMapper;
import com.example.LuanVanTotNghiep.mapper.UniversitiesMapper;
import com.example.LuanVanTotNghiep.repository.BranchsRepository;
import com.example.LuanVanTotNghiep.repository.GroupsRepository;
import com.example.LuanVanTotNghiep.repository.UniversitiesRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BranchsService {
    BranchsRepository branchsRepository;
    BranchsMapper branchsMapper;
    UniversitiesRepository universitiesRepository;
    UniversitiesMapper universitiesMapper;
    GroupsRepository groupsRepository;
    @Transactional
    public BranchsResponse createBranch(BranchsRequest request) {
         // Kiểm tra branch_id đã tồn tại
        if (branchsRepository.existsById(request.getBranch_id())) {

            throw new AppException(ErrorCode.BRANCHID_EXISTS);
        }
        // Kiểm tra branchname đã tồn tại
        if (branchsRepository.existsByBranchname(request.getBranchname())) {

            throw new AppException(ErrorCode.BRANCHNAME_EXISTS);
        }
        // Tìm Universities theo university_id
        Universities university = universitiesRepository.findById(request.getUniversity_id())
                .orElseThrow(() -> {

                    return new AppException(ErrorCode.UNIVERSITYID_NOT_FOUND);
                });

        // Ánh xạ BranchsRequest sang Branchs
        Branchs branch = branchsMapper.toCreateBranch(request);
        branch.setUniversity(university);

        // Lưu Branchs
        try {
            branch = branchsRepository.save(branch);

        } catch (Exception e) {

            throw new AppException(ErrorCode.DATABASE_ERROR);
        }

        // Ánh xạ Branchs sang BranchsResponse
        BranchsResponse response = branchsMapper.toBranchResponse(branch);
        // Ánh xạ Universities sang UniversitiesResponse
        response.setUniversityResponse(universitiesMapper.toUniversityResponse(university));

        return response;
    }
    public List<BranchsResponse> getAllBranchs() {
        List<Branchs> branches = branchsRepository.findAll();
        List<BranchsResponse> responses = branches.stream()
                .map(branch -> {
                    BranchsResponse response = branchsMapper.toBranchResponse(branch);
                    response.setUniversityResponse(universitiesMapper.toUniversityResponse(branch.getUniversity()));
                    return response;
                })
                .collect(Collectors.toList());

        return responses;
    }
    @Transactional
    public Branchs_GroupsResponse createBranchs_Groups(Branchs_GroupsRequest request) {
        // 1. Kiểm tra đầu vào
        if (request.getGroup_ids() == null || request.getGroup_ids().isEmpty()) {
            throw new AppException(ErrorCode.DATA_NOT_FOUND);
        }

        // 2. Tìm Branchs kèm groups
        Branchs branch = branchsRepository.findByIdWithGroups(request.getBranch_id())
                .orElseThrow(() -> new AppException(ErrorCode.BRANCHID_NOT_FOUND));

        // 3. Đảm bảo tập hợp groups được khởi tạo
        if (branch.getGroups() == null) {
            branch.setGroups(new HashSet<>());
        }

        // 4. Tìm tất cả Groups theo danh sách group_ids
        Set<Groups> groups = request.getGroup_ids().stream()
                .map(groupId -> groupsRepository.findById(groupId)
                        .orElseThrow(() -> new AppException(ErrorCode.CONTENT_NO_EXISTS)))
                .collect(Collectors.toSet());

        // 5. Thêm groups mới, tránh trùng lặp
        branch.getGroups().addAll(groups.stream()
                .filter(group -> !branch.getGroups().contains(group))
                .collect(Collectors.toSet()));

        // 6. Lưu branch để cập nhật quan hệ
        Branchs updatedBranch = branchsRepository.save(branch);

        // 7. Chuyển đổi và trả về BranchsResponse
        return branchsMapper.toBranchsGroupsResponse(updatedBranch);
    }
    @Transactional
    public void deleteBranch_Groups(Branchs_GroupsRequest request) {
        // Logger để theo dõi

        // 1. Kiểm tra đầu vào
        if (request.getBranch_id() == null || request.getBranch_id().isEmpty()) {
            throw new AppException(ErrorCode.DATA_NOT_FOUND);
        }
        if (request.getGroup_ids() == null || request.getGroup_ids().isEmpty()) {
            throw new AppException(ErrorCode.DATA_NOT_FOUND);
        }

        // 2. Tìm Branchs kèm groups
        Branchs branch = branchsRepository.findByIdWithGroups(request.getBranch_id())
                .orElseThrow(() -> {
                    return new AppException(ErrorCode.BRANCHID_NOT_FOUND);
                });

        // 3. Đảm bảo tập hợp groups được khởi tạo đầy đủ
        if (branch.getGroups() == null) {
            branch.setGroups(new HashSet<>());
        } else {
            Hibernate.initialize(branch.getGroups()); // Khởi tạo đầy đủ tập hợp
        }

        // 4. Tìm tất cả Groups theo danh sách group_ids
        Set<Groups> groupsToRemove = new HashSet<>(groupsRepository.findAllById(request.getGroup_ids()));
        if (groupsToRemove.size() != request.getGroup_ids().size()) {
            throw new AppException(ErrorCode.CONTENT_NO_EXISTS);
        }

        // 5. Tạo bản sao của tập hợp groups để sửa đổi
        Set<Groups> updatedGroups = new HashSet<>(branch.getGroups());
        boolean removed = updatedGroups.removeAll(groupsToRemove);

        // 6. Kiểm tra xem có nhóm nào được xóa không
        if (!removed) {
            throw new AppException(ErrorCode.NO_GROUPS_REMOVED);
        }

        // 7. Cập nhật tập hợp groups trong branch
        branch.setGroups(updatedGroups);

        // 8. Lưu branch để cập nhật quan hệ
        branchsRepository.save(branch);

    }
}