package com.example.LuanVanTotNghiep.service;

import com.example.LuanVanTotNghiep.dto.request.SchoolsRequest;
import com.example.LuanVanTotNghiep.dto.response.SchoolsResponse;
import com.example.LuanVanTotNghiep.entity.Provinces;
import com.example.LuanVanTotNghiep.entity.Schools;
import com.example.LuanVanTotNghiep.exception.AppException;
import com.example.LuanVanTotNghiep.exception.ErrorCode;
import com.example.LuanVanTotNghiep.mapper.SchoolsMapper;
import com.example.LuanVanTotNghiep.repository.ProvincesRepository;
import com.example.LuanVanTotNghiep.repository.SchoolsRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class SchoolsService {
    SchoolsRepository schoolsRepository;
    SchoolsMapper schoolsMapper;
    ProvincesRepository provincesRepository;

    public List<SchoolsResponse> getAllSchools() {
        List<Schools> schools = schoolsRepository.findAll();
        return schools.stream().map(school -> {
            SchoolsResponse response = schoolsMapper.toSchoolsResponse(school);
            response.setProvince_id(school.getProvince() != null ? school.getProvince().getProvince_id() : "");
            return response;
        }).collect(Collectors.toList());
    }

    public List<SchoolsResponse> getSchoolsByProvinceId(String provinceId) {
        if (!provincesRepository.existsById(provinceId)) {
            throw new AppException(ErrorCode.DATA_NOT_FOUND, "Không tìm thấy mã vùng: " + provinceId);
        }
        List<Schools> schools = schoolsRepository.findByProvinces_ProvinceId(provinceId);
        return schools.stream().map(school -> {
            SchoolsResponse response = schoolsMapper.toSchoolsResponse(school);
            response.setProvince_id(school.getProvince() != null ? school.getProvince().getProvince_id() : "");
            return response;
        }).collect(Collectors.toList());
    }

    public List<SchoolsResponse> importSchoolsFromExcel(MultipartFile file) {
        if (file.isEmpty()) {
            throw new AppException(ErrorCode.REQUEST_IS_EMPTY);
        }

        List<Schools> schoolsList = new ArrayList<>();
        Set<String> existingSchoolIds = new HashSet<>(schoolsRepository.findAll().stream().map(Schools::getSchool_id).toList());
        Set<String> duplicateIds = new HashSet<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // Lấy sheet đầu tiên
            Iterator<Row> rowIterator = sheet.iterator();

            // Bỏ qua header
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getCell(0) == null || row.getCell(2) == null) {
                    continue; // Bỏ qua dòng trống
                }

                String provinceId = getCellValueAsString(row.getCell(0));
                String schoolId = getCellValueAsString(row.getCell(2));
                String schoolName = row.getCell(3).getStringCellValue().trim();

                if (!provincesRepository.existsById(provinceId)) {
                    continue;
                }

                String fullSchoolId = provinceId + "_" + schoolId;

                if (existingSchoolIds.contains(fullSchoolId)) {
                    duplicateIds.add(schoolName);
                    continue; // Bỏ qua trường đã tồn tại
                }

                SchoolsRequest request = SchoolsRequest.builder()
                        .school_id(fullSchoolId)
                        .school_name(schoolName)
                        .province_id(provinceId)
                        .build();

                Schools school = schoolsMapper.toCreateSchools(request);
                Provinces province = provincesRepository.findById(provinceId)
                        .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND, "Không tìm thấy mã vùng: " + provinceId));
                school.setProvince(province);
                schoolsList.add(school);
            }

            if (!duplicateIds.isEmpty()) {
                throw new AppException(ErrorCode.DATA_ALREADY_EXISTS, "Trường đã tồn tại: " + String.join(", ", duplicateIds));
            }

            List<Schools> savedSchools = new ArrayList<>(schoolsRepository.saveAll(schoolsList));
            List<SchoolsResponse> responses = savedSchools.stream().map(school -> {
                SchoolsResponse response = schoolsMapper.toSchoolsResponse(school);
                response.setProvince_id(school.getProvince() != null ? school.getProvince().getProvince_id() : "");
                return response;
            }).collect(Collectors.toList());
            return responses;
        } catch (IOException e) {
            throw new AppException(ErrorCode.UNEXPECTED_ERROR);
        }
    }

    public SchoolsResponse createSchool(SchoolsRequest request) {
        if (request.getSchool_id() == null || request.getSchool_id().trim().isEmpty()) {
            throw new AppException(ErrorCode.REQUEST_IS_EMPTY);
        }
        if (request.getSchool_name() == null || request.getSchool_name().trim().isEmpty()) {
            throw new AppException(ErrorCode.REQUEST_IS_EMPTY);
        }
        if (!provincesRepository.existsById(request.getProvince_id())) {
            throw new AppException(ErrorCode.DATA_NOT_FOUND, "Không tìm thấy mã vùng: " + request.getProvince_id());
        }
        if (schoolsRepository.existsById(request.getSchool_id())) {
            throw new AppException(ErrorCode.DATA_ALREADY_EXISTS);
        }

        Schools school = schoolsMapper.toCreateSchools(request);
        Provinces province = provincesRepository.findById(request.getProvince_id())
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        school.setSchool_id(request.getProvince_id() + "_" + request.getSchool_id());
        school.setProvince(province);

        Schools savedSchool = schoolsRepository.save(school);
        return schoolsMapper.toSchoolsResponse(savedSchool);
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue()).trim();
            case BLANK:
                return "";
            default:
                throw new AppException(ErrorCode.INVALID_INPUT);
        }
    }
}
