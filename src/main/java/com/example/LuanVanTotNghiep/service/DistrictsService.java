package com.example.LuanVanTotNghiep.service;

import com.example.LuanVanTotNghiep.dto.request.DistrictsRequest;
import com.example.LuanVanTotNghiep.dto.response.DistrictsResponse;
import com.example.LuanVanTotNghiep.entity.Districts;
import com.example.LuanVanTotNghiep.entity.Provinces;
import com.example.LuanVanTotNghiep.exception.AppException;
import com.example.LuanVanTotNghiep.exception.ErrorCode;
import com.example.LuanVanTotNghiep.mapper.DistrictsMapper;
import com.example.LuanVanTotNghiep.repository.DistrictsRepository;
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
public class DistrictsService {
    DistrictsRepository districtsRepository;
    DistrictsMapper districtsMapper;
    ProvincesRepository provincesRepository;
    ConvertService convertService;

    public List<DistrictsResponse> getAllDistricts() {
        List<Districts> districts = districtsRepository.findAll();

        if (districts.isEmpty()){
            throw new AppException(ErrorCode.DATA_NOT_FOUND, "Danh sách quận/huyện trống");
        }

        return districts.stream().map(district -> {
            DistrictsResponse response = districtsMapper.toDistrictsResponse(district);
            response.setProvince_id(district.getProvince() != null ? district.getProvince().getProvince_id() : "");
            return response;
        }).collect(Collectors.toList());
    }

    public List<DistrictsResponse> getListDistrictsByProvinceId(String provinceId) {
        if (provinceId == null || provinceId.trim().isEmpty()) {
            throw new AppException(ErrorCode.REQUEST_IS_EMPTY);
        }
        if (!provincesRepository.existsById(provinceId)) {
            throw new AppException(ErrorCode.DATA_NOT_FOUND, "Không tìm thấy mã tỉnh/thành: " + provinceId);
        }

        List<Districts> districts = districtsRepository.findByProvince_ProvinceId(provinceId);

        if (districts.isEmpty()){
            throw new AppException(ErrorCode.DATA_NOT_FOUND, "Danh sách quận/huyện trống");
        }

        return districts.stream().map(district -> {
            DistrictsResponse response = districtsMapper.toDistrictsResponse(district);
            response.setProvince_id(district.getProvince() != null ? district.getProvince().getProvince_id() : "");
            return response;
        }).collect(Collectors.toList());
    }

    @Transactional
    public DistrictsResponse createDistrict(DistrictsRequest request) {
        if (!provincesRepository.existsById(request.getProvince_id())) {
            throw new AppException(ErrorCode.DATA_NOT_FOUND, "Không tìm thấy mã vùng: " + request.getProvince_id());
        }

        String id = request.getProvince_id() + "_" + request.getDistrict_id();
        if (districtsRepository.existsById(id)) {
            throw new AppException(ErrorCode.DATA_ALREADY_EXISTS, "Quận/huyện đã tồn tại");
        }

        Provinces province = provincesRepository.findById(request.getProvince_id())
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        Districts district = districtsMapper.toCreateDistricts(request);
        district.setId(id);
        district.setProvince(province);

        Districts savedDistrict = districtsRepository.save(district);
        DistrictsResponse response = districtsMapper.toDistrictsResponse(savedDistrict);
        response.setProvince_id(province.getProvince_id() != null ? province.getProvince_id() : null);

        return response;
    }

    @Transactional
    public List<DistrictsResponse> importDistrictsFromExcel(MultipartFile file) {
        if (file.isEmpty()) {
            throw new AppException(ErrorCode.REQUEST_IS_EMPTY);
        }

        List<Districts> districtsList = new ArrayList<>();
        Set<String> existingIds = new HashSet<>(districtsRepository.findAll().stream().map(Districts::getId).toList());
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

                String provinceId = convertService.convertToString(row.getCell(0));
                String districtId = convertService.convertToString(row.getCell(2));
                String districtName = row.getCell(3).getStringCellValue().trim();

                String id = provinceId + "_" + districtId;

                if (existingIds.contains(id)) {
                    duplicateIds.add(districtName);
                    continue;
                }

                DistrictsRequest request = DistrictsRequest.builder()
                        .id(id)
                        .district_id(districtId)
                        .district_name(districtName)
                        .province_id(provinceId)
                        .build();

                Districts district = districtsMapper.toCreateDistricts(request);
                Provinces province = provincesRepository.findById(provinceId)
                        .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND, "Không tìm thấy mã vùng: " + provinceId));
                district.setProvince(province);
                districtsList.add(district);
            }

            if (!duplicateIds.isEmpty()) {
                throw new AppException(ErrorCode.DATA_ALREADY_EXISTS, "Quận/Huyện đã tồn tại: " + String.join(", ", duplicateIds));
            }

            List<Districts> savedDistricts = new ArrayList<>(districtsRepository.saveAll(districtsList));
            List<DistrictsResponse> responses = savedDistricts.stream().map(district -> {
                DistrictsResponse response = districtsMapper.toDistrictsResponse(district);
                response.setProvince_id(district.getProvince() != null ? district.getProvince().getProvince_id() : "");
                return response;
            }).collect(Collectors.toList());
            return responses;
        } catch (IOException e) {
            throw new AppException(ErrorCode.UNEXPECTED_ERROR);
        }
    }

    @Transactional
    public void deleteProvince(String province_id, String id) {
        if (!provincesRepository.existsById(province_id)) {
            throw new AppException(ErrorCode.DATA_NOT_FOUND, "Không tìm thấy mã tỉnh/thành: " + province_id);
        }

        Districts district = districtsRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND, "Không tìm thấy quận/huyện: " + id));

        // Kiểm tra xem district_id có thuộc province_id không
        if (!district.getProvince().getProvince_id().equals(province_id)) {
            throw new AppException(ErrorCode.CONSTRAINT_VIOLATION,
                    "Mã quận/huyện " + id + " không thuộc tỉnh/thành " + province_id);
        }

        districtsRepository.deleteById(id);
    }

    @Transactional
    public  void deleteListProvinces(String province_id){
        if (!provincesRepository.existsById(province_id)) {
            throw new AppException(ErrorCode.DATA_NOT_FOUND, "Không tìm thấy mã tỉnh/thành: " + province_id);
        }

        List<Districts> districtsListToDelete = districtsRepository.findByProvince_ProvinceId(province_id);
        if (districtsListToDelete.isEmpty()){
            throw new AppException(ErrorCode.DATA_NOT_FOUND, "Không có quận/huyện nào trong tỉnh/thành");
        }

        districtsRepository.deleteAll(districtsListToDelete);

    }

    @Transactional
    public DistrictsResponse updateDistricts(String province_id, String id, DistrictsRequest request) {
        if (!provincesRepository.existsById(province_id)) {
            throw new AppException(ErrorCode.DATA_NOT_FOUND, "Không tìm thấy mã tỉnh/thành: " + province_id);
        }

        Districts district = districtsRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND, "Không tìm thấy mã quận/huyện: " + id));

        // Kiểm tra xem district_id có thuộc province_id không
        if (!district.getProvince().getProvince_id().equals(province_id)) {
            throw new AppException(ErrorCode.CONSTRAINT_VIOLATION,
                    "Mã quận/huyện " + id + " không thuộc tỉnh/thành " + province_id);
        }

        // Cập nhật thông tin
        districtsMapper.updateDistrict(district, request);
        Provinces province = provincesRepository.findById(province_id)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        district.setProvince(province);

        Districts updatedDistrict = districtsRepository.save(district);
        updatedDistrict.setId(id);
        DistrictsResponse response = districtsMapper.toDistrictsResponse(updatedDistrict);
        response.setProvince_id(province.getProvince_id() != null ? province.getProvince_id() : null);

        return response;
    }

//    private String normalizeDistrictId(String districtName) {
//        if (districtName == null) return "";
//        return removeDiacritics(districtName.toLowerCase())
//                .replaceAll("[^a-z0-9]", "") // Loại bỏ ký tự đặc biệt và khoảng trắng
//                .replaceAll("\\s+", "");
//    }
//
//    private String removeDiacritics(String str) {
//        if (str == null) return "";
//        return str.chars()
//                .mapToObj(ch -> String.valueOf((char) ch))
//                .map(this::normalizeCharacter)
//                .collect(Collectors.joining());
//    }
//
//    private String normalizeCharacter(String ch) {
//        switch (ch.toLowerCase()) {
//            // Chữ cái "a" và các biến thể
//            case "à": case "á": case "ả": case "ã": case "ạ":
//            case "ă": case "ằ": case "ắ": case "ẳ": case "ẵ": case "ặ":
//            case "â": case "ầ": case "ấ": case "ẩ": case "ẫ": case "ậ":
//                return "a";
//            // Chữ cái "e" và các biến thể
//            case "è": case "é": case "ẻ": case "ẽ": case "ẹ":
//            case "ê": case "ề": case "ế": case "ể": case "ễ": case "ệ":
//                return "e";
//            // Chữ cái "i" và các biến thể
//            case "ì": case "í": case "ỉ": case "ĩ": case "ị":
//                return "i";
//            // Chữ cái "o" và các biến thể
//            case "ò": case "ó": case "ỏ": case "õ": case "ọ":
//            case "ô": case "ồ": case "ố": case "ổ": case "ỗ": case "ộ":
//            case "ơ": case "ờ": case "ớ": case "ở": case "ỡ": case "ợ":
//                return "o";
//            // Chữ cái "u" và các biến thể
//            case "ù": case "ú": case "ủ": case "ũ": case "ụ":
//            case "ư": case "ừ": case "ứ": case "ử": case "ữ": case "ự":
//                return "u";
//            // Chữ cái "y" và các biến thể
//            case "ỳ": case "ý": case "ỷ": case "ỹ": case "ỵ":
//                return "y";
//            // Chữ cái "đ"
//            case "đ":
//                return "d";
//            // Chữ cái viết hoa đầy đủ
//            case "À": case "Á": case "Ả": case "Ã": case "Ạ":
//            case "Ă": case "Ằ": case "Ắ": case "Ẳ": case "Ẵ": case "Ặ":
//            case "Â": case "Ầ": case "Ấ": case "Ẩ": case "Ẫ": case "Ậ":
//            case "È": case "É": case "Ẻ": case "Ẽ": case "Ẹ":
//            case "Ê": case "Ề": case "Ế": case "Ể": case "Ễ": case "Ệ":
//            case "Ì": case "Í": case "Ỉ": case "Ĩ": case "Ị":
//            case "Ò": case "Ó": case "Ỏ": case "Õ": case "Ọ":
//            case "Ô": case "Ồ": case "Ố": case "Ổ": case "Ỗ": case "Ộ":
//            case "Ơ": case "Ờ": case "Ớ": case "Ở": case "Ỡ": case "Ợ":
//            case "Ù": case "Ú": case "Ủ": case "Ũ": case "Ụ":
//            case "Ư": case "Ừ": case "Ứ": case "Ử": case "Ữ": case "Ự":
//            case "Ỳ": case "Ý": case "Ỷ": case "Ỹ": case "Ỵ":
//            case "Đ":
//                return ch.toLowerCase()
//                        .replaceAll("[àáảãạăằắẳẵặâầấẩẫậ]", "a")
//                        .replaceAll("[èéẻẽẹêềếểễệ]", "e")
//                        .replaceAll("[ìíỉĩị]", "i")
//                        .replaceAll("[òóỏõọôồốổỗộơờớởỡợ]", "o")
//                        .replaceAll("[ùúủũụưừứửữự]", "u")
//                        .replaceAll("[ỳýỷỹỵ]", "y")
//                        .replaceAll("đ", "d");
//            default:
//                return ch;
//        }
//    }
}
