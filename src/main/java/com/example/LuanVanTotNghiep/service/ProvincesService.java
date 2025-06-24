package com.example.LuanVanTotNghiep.service;

import com.example.LuanVanTotNghiep.dto.request.ProvincesRequest;
import com.example.LuanVanTotNghiep.dto.response.ProvincesResponse;
import com.example.LuanVanTotNghiep.entity.Districts;
import com.example.LuanVanTotNghiep.entity.Provinces;
import com.example.LuanVanTotNghiep.exception.AppException;
import com.example.LuanVanTotNghiep.exception.ErrorCode;
import com.example.LuanVanTotNghiep.mapper.ProvincesMapper;
import com.example.LuanVanTotNghiep.repository.ProvincesRepository;
import jakarta.transaction.Transactional;
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
public class ProvincesService {
    ProvincesRepository provincesRepository;
    ProvincesMapper provincesMapper;
    ConvertService convertService;

    public List<ProvincesResponse> getAllProvinces() {
        List<Provinces> provincesList = provincesRepository.findAll();
        return provincesMapper.listProvinces(provincesList);
    }

    public ProvincesResponse getProvinceById(String provinceId) {
        if (!provincesRepository.existsById(provinceId)) {
            throw new AppException(ErrorCode.DATA_NOT_FOUND, "Không tìm thấy mã tỉnh/thành: " + provinceId);
        }
        Provinces province = provincesRepository.findById(provinceId).orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        return provincesMapper.toProvinceResponse(province);
    }

    @Transactional
    public ProvincesResponse createProvince(ProvincesRequest request) {
        if (request.getProvince_id() == null || request.getProvince_id().trim().isEmpty()) {
            throw new AppException(ErrorCode.REQUEST_IS_EMPTY);
        }
        if (request.getProvince_name() == null || request.getProvince_name().trim().isEmpty()) {
            throw new AppException(ErrorCode.REQUEST_IS_EMPTY);
        }
        if (provincesRepository.existsById(request.getProvince_id())) {
            throw new AppException(ErrorCode.DATA_ALREADY_EXISTS);
        }

        Provinces province = provincesMapper.toCreateProvinces(request);
        Provinces savedProvince = provincesRepository.save(province);

        return provincesMapper.toProvinceResponse(savedProvince);
    }

    @Transactional
    public List<ProvincesResponse> importProvincesFromExcel(MultipartFile file) {
        if (file.isEmpty()) {
            throw new AppException(ErrorCode.REQUEST_IS_EMPTY);
        }

        List<Provinces> provincesList = new ArrayList<>();
        Set<String> existingProvinceIds = new HashSet<>(provincesRepository.findAll().stream().map(Provinces::getProvince_id).toList());
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

                String provinceId = convertService.convertToString(row.getCell(0));
                String provinceName = row.getCell(1).getStringCellValue().trim();

                if (existingProvinceIds.contains(provinceId)) {
                    duplicateIds.add(provinceName);
                    continue; // Bỏ qua mã vùng đã tồn tại
                }

                ProvincesRequest request = ProvincesRequest.builder()
                        .province_id(provinceId)
                        .province_name(provinceName)
                        .build();

                Provinces province = provincesMapper.toCreateProvinces(request);
                provincesList.add(province);
            }

            if (!duplicateIds.isEmpty()) {
                throw new AppException(ErrorCode.DATA_ALREADY_EXISTS, "Tỉnh/Thành phố đã tồn tại: " + String.join(", ", duplicateIds));
            }

            List<Provinces> savedProvince = new ArrayList<>(provincesRepository.saveAll(provincesList));
            return savedProvince.stream().map(provincesMapper::toProvinceResponse).collect(Collectors.toList());
        } catch (IOException e) {
            throw new AppException(ErrorCode.UNEXPECTED_ERROR);
        }
    }


}
