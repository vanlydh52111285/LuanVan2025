package com.example.LuanVanTotNghiep.service;

import com.example.LuanVanTotNghiep.dto.request.SubjectsRequest;
import com.example.LuanVanTotNghiep.dto.response.SubjectResponse;
import com.example.LuanVanTotNghiep.entity.Subjects;
import com.example.LuanVanTotNghiep.exception.AppException;
import com.example.LuanVanTotNghiep.exception.ErrorCode;
import com.example.LuanVanTotNghiep.mapper.SubjectsMapper;
import com.example.LuanVanTotNghiep.repository.SubjectsRepository;
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

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubjectsService {

    SubjectsRepository subjectsRepository;
    SubjectsMapper subjectsMapper;
    ConvertService convertService;

    @Transactional
    public SubjectResponse createSubject(SubjectsRequest request) {
        if (request.getSub_id() == null || request.getSub_id().trim().isEmpty()) {
            throw new AppException(ErrorCode.REQUEST_IS_EMPTY);
        }
        if (request.getSub_name() == null || request.getSub_name().trim().isEmpty()) {
            throw new AppException(ErrorCode.REQUEST_IS_EMPTY);
        }
        if (subjectsRepository.existsById(request.getSub_id())) {
            throw new AppException(ErrorCode.DATA_ALREADY_EXISTS);
        }

        Subjects subject = subjectsMapper.toCreateSubjects(request);
        Subjects savedSubject = subjectsRepository.save(subject);

        return subjectsMapper.toSubjectResponse(savedSubject);
    }

    @Transactional
    public List<SubjectResponse> importSubjectsFromExcel(MultipartFile file) {
        if (file.isEmpty()) {
            throw new AppException(ErrorCode.REQUEST_IS_EMPTY);
        }

        List<Subjects> subjectsList = new ArrayList<>();
        Set<String> existingSubjectsIds = new HashSet<>(subjectsRepository.findAll().stream().map(Subjects::getSub_id).toList());
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

                String sub_id = row.getCell(2).getStringCellValue().trim();
                String sub_name = row.getCell(1).getStringCellValue().trim();
                boolean sub_sgk_2006 = convertService.convertToBoolean(row.getCell(3));
                boolean sub_sgk_2018 = convertService.convertToBoolean(row.getCell(4));

                if (existingSubjectsIds.contains(sub_id)) {
                    duplicateIds.add(sub_name);
                    continue; // Bỏ qua mã vùng đã tồn tại
                }

                SubjectsRequest request = SubjectsRequest.builder()
                        .sub_id(sub_id)
                        .sub_name(sub_name)
                        .sub_sgk_2006(sub_sgk_2006)
                        .sub_sgk_2018(sub_sgk_2018)
                        .build();

                Subjects subject = subjectsMapper.toCreateSubjects(request);
                subjectsList.add(subject);
            }

            if (!duplicateIds.isEmpty()) {
                throw new AppException(ErrorCode.DATA_ALREADY_EXISTS, "Môn học đã tồn tại: " + String.join(", ", duplicateIds));
            }

            List<Subjects> saveSubjects = new ArrayList<>(subjectsRepository.saveAll(subjectsList));
            return saveSubjects.stream().map(subjectsMapper::toSubjectResponse).collect(Collectors.toList());
        } catch (IOException e) {
            throw new AppException(ErrorCode.UNEXPECTED_ERROR);
        }
    }
}
