package com.example.LuanVanTotNghiep.service;

import com.example.LuanVanTotNghiep.dto.request.BranchsRequest;
import com.example.LuanVanTotNghiep.dto.request.Branchs_GroupsRequest;
import com.example.LuanVanTotNghiep.dto.request.Branchs_ProgramsRequest;
import com.example.LuanVanTotNghiep.dto.response.*;
import com.example.LuanVanTotNghiep.entity.Branchs;
import com.example.LuanVanTotNghiep.entity.Groups;
import com.example.LuanVanTotNghiep.entity.Programs;
import com.example.LuanVanTotNghiep.entity.Universities;
import com.example.LuanVanTotNghiep.exception.AppException;
import com.example.LuanVanTotNghiep.exception.ErrorCode;
import com.example.LuanVanTotNghiep.mapper.BranchsMapper;
import com.example.LuanVanTotNghiep.mapper.GroupsMapper;
import com.example.LuanVanTotNghiep.mapper.ProgramsMapper;
import com.example.LuanVanTotNghiep.mapper.UniversitiesMapper;
import com.example.LuanVanTotNghiep.repository.BranchsRepository;
import com.example.LuanVanTotNghiep.repository.GroupsRepository;
import com.example.LuanVanTotNghiep.repository.ProgramsRepository;
import com.example.LuanVanTotNghiep.repository.UniversitiesRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
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
    GroupsMapper groupsMapper;
    ProgramsRepository programsRepository;
    ProgramsMapper programsMapper;
    ConvertService convertService;

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
    public List<Branchs_Entity_Response> getAllBranchs() {
        return branchsMapper.lBranchsEntityResponses(branchsRepository.findAll());
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
    public List<BranchsResponse> importBranchsFromExcel(MultipartFile file) {
        if (file.isEmpty()) {
            throw new AppException(ErrorCode.REQUEST_IS_EMPTY);
        }

        List<Branchs> branchsList = new ArrayList<>();
        Set<String> existingBranchIds = new HashSet<>(branchsRepository.findAll().stream().map(Branchs::getBranch_id).toList());
        Set<String> duplicateNames = new HashSet<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // Lấy sheet đầu tiên (Sheet1)
            Iterator<Row> rowIterator = sheet.iterator();

            // Bỏ qua header
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getCell(0) == null || row.getCell(1) == null || row.getCell(2) == null) {
                    continue; // Bỏ qua dòng trống
                }

                String university_id = row.getCell(1).getStringCellValue().trim();
                String branchId = convertService.convertToString(row.getCell(2)).trim();
                String branchName = row.getCell(3).getStringCellValue().trim();
                boolean status = convertService.convertToBoolean(row.getCell(4));

                if (existingBranchIds.contains(branchId)) {
                    duplicateNames.add(branchName);
                    continue; // Bỏ qua mã ngành đã tồn tại
                }

                BranchsRequest request = BranchsRequest.builder()
                        .branch_id(branchId)
                        .branchname(branchName)
                        .status(status)
                        .university_id(university_id)
                        .build();

                Branchs branch = branchsMapper.toCreateBranch(request);
                Universities university = universitiesRepository.findById(university_id)
                                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND, "Không tìm thấy trường đại học có mã: " + university_id));
                branch.setUniversity(university);
                branchsList.add(branch);
            }

            if (!duplicateNames.isEmpty()) {
                throw new AppException(ErrorCode.DATA_ALREADY_EXISTS, "Ngành học đã tồn tại: " + String.join(", ", duplicateNames));
            }

            List<Branchs> savedBranchs = new ArrayList<>(branchsRepository.saveAll(branchsList));
            return savedBranchs.stream().map(branchsMapper::toBranchResponse).collect(Collectors.toList());
        } catch (IOException e) {
            throw new AppException(ErrorCode.UNEXPECTED_ERROR);
        }
    }

    @Transactional
    public void deleteBranch(String branchId) {
        if (!branchsRepository.existsById(branchId)) {
            throw new AppException(ErrorCode.BRANCHID_NOT_FOUND, "Không tìm thấy ngành học với mã: " + branchId);
        }

        branchsRepository.deleteById(branchId);
    }

    @Transactional
    public BranchsResponse updateBranch(String branchId, BranchsRequest request) {
        if (branchId == null || branchId.trim().isEmpty()) {
            throw new AppException(ErrorCode.REQUEST_IS_EMPTY);
        }
        if (!branchsRepository.existsById(branchId)) {
            throw new AppException(ErrorCode.BRANCHID_NOT_FOUND, "Không tìm thấy ngành học với mã: " + branchId);
        }

        Branchs branch = branchsRepository.findById(branchId)
                .orElseThrow(() -> new AppException(ErrorCode.BRANCHID_NOT_FOUND));

        // Cập nhật thông tin
        branchsMapper.updateBranch(branch, request);
        Universities university = universitiesRepository.findById(request.getUniversity_id())
                .orElseThrow(() -> new AppException(ErrorCode.UNIVERSITYID_NOT_FOUND));
        branch.setUniversity(university);

        Branchs updatedBranch = branchsRepository.save(branch);
        BranchsResponse response = branchsMapper.toBranchResponse(updatedBranch);
        response.setUniversityResponse(universitiesMapper.toUniversityResponse(university));

        return response;
    }
//    @Transactional
//    public void deleteBranch_Groups(Branchs_GroupsRequest request) {
//        // Logger để theo dõi
//
//        // 1. Kiểm tra đầu vào
//        if (request.getBranch_id() == null || request.getBranch_id().isEmpty()) {
//            throw new AppException(ErrorCode.DATA_NOT_FOUND);
//        }
//        if (request.getGroup_ids() == null || request.getGroup_ids().isEmpty()) {
//            throw new AppException(ErrorCode.DATA_NOT_FOUND);
//        }
//
//        // 2. Tìm Branchs kèm groups
//        Branchs branch = branchsRepository.findByIdWithGroups(request.getBranch_id())
//                .orElseThrow(() -> {
//                    return new AppException(ErrorCode.BRANCHID_NOT_FOUND);
//                });
//
//        // 3. Đảm bảo tập hợp groups được khởi tạo đầy đủ
//        if (branch.getGroups() == null) {
//            branch.setGroups(new HashSet<>());
//        } else {
//            Hibernate.initialize(branch.getGroups()); // Khởi tạo đầy đủ tập hợp
//        }
//
//        // 4. Tìm tất cả Groups theo danh sách group_ids
//        Set<Groups> groupsToRemove = new HashSet<>(groupsRepository.findAllById(request.getGroup_ids()));
//        if (groupsToRemove.size() != request.getGroup_ids().size()) {
//            throw new AppException(ErrorCode.CONTENT_NO_EXISTS);
//        }
//
//        // 5. Tạo bản sao của tập hợp groups để sửa đổi
//        Set<Groups> updatedGroups = new HashSet<>(branch.getGroups());
//        boolean removed = updatedGroups.removeAll(groupsToRemove);
//
//        // 6. Kiểm tra xem có nhóm nào được xóa không
//        if (!removed) {
//            throw new AppException(ErrorCode.NO_GROUPS_REMOVED);
//        }
//
//        // 7. Cập nhật tập hợp groups trong branch
//        branch.setGroups(updatedGroups);
//
//        // 8. Lưu branch để cập nhật quan hệ
//        branchsRepository.save(branch);
//
//    }
    @Transactional
    public Branchs_ProgramsResponse createBranchProgram(Branchs_ProgramsRequest request) {
        // 1. Kiểm tra đầu vào
        if (request.getProgram_ids() == null || request.getProgram_ids().isEmpty()) {
            throw new AppException(ErrorCode.DATA_NOT_FOUND);
        }

        // 2. Tìm Branchs kèm programs
        Branchs branch = branchsRepository.findByIdWithPrograms(request.getBranch_id())
                .orElseThrow(() -> new AppException(ErrorCode.BRANCHID_NOT_FOUND));

        // 3. Đảm bảo tập hợp programs được khởi tạo
        if (branch.getPrograms() == null) {
            branch.setPrograms(new HashSet<>());
        }

        // 4. Tìm tất cả Programs theo danh sách program_ids
        Set<Programs> programs = request.getProgram_ids().stream()
                .map(programId -> programsRepository.findById(programId)
                        .orElseThrow(() -> new AppException(ErrorCode.PROGRAMID_NOT_FOUND)))
                .collect(Collectors.toSet());

        // 5. Thêm programs mới, tránh trùng lặp
        branch.getPrograms().addAll(programs.stream()
                .filter(program -> !branch.getPrograms().contains(program))
                .collect(Collectors.toSet()));

        // 6. Lưu branch để cập nhật quan hệ
        Branchs updatedBranch = branchsRepository.save(branch);

        // 7. Tải đầy đủ programs trước khi ánh xạ
        Hibernate.initialize(updatedBranch.getPrograms());

        // 8. Chuyển đổi và trả về Branchs_ProgramsResponse
        return branchsMapper.toBranchsProgramsResponse(updatedBranch);
    }
    public List<GroupsResponse> getGroupsByBranch(String branch_id){
        if (!branchsRepository.existsById(branch_id)){
            throw new AppException(ErrorCode.BRANCHID_NOT_FOUND);
        }
        List<GroupsResponse> list = groupsMapper.listGroups(branchsRepository.findByGroupsByBranchId(branch_id));
        return list;
    }
    public List<ProgramsResponse> getProgramsByBranch(String branch_id){
        if (!branchsRepository.existsById(branch_id)){
            throw new AppException(ErrorCode.BRANCHID_NOT_FOUND);
        }
        List<ProgramsResponse> list = programsMapper.listPrograms(branchsRepository.findByProgramsByBranchId(branch_id));
        return list;
    }
    public List<Branchs_Entity_Response> getBranchsByProgram(String program_id){
        if (!programsRepository.existsById(program_id)){
            throw new AppException(ErrorCode.PROGRAMID_NOT_FOUND);
        }
        return branchsMapper.lBranchsEntityResponses(branchsRepository.findBranchsByProgramId(program_id));
    }
    @Transactional(readOnly = true)
    public List<Branchs_ProgramsResponse> getBranchsByGroupsAndPrograms() {
        // Lấy danh sách Branchs từ repository
        List<Branchs> branches = branchsRepository.findBranchesWithAtLeastOneGroupAndProgram();
        return branchsMapper.lBranchsProgramsResponses(branches);
    }
}