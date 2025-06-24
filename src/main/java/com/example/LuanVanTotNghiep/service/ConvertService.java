package com.example.LuanVanTotNghiep.service;

import com.example.LuanVanTotNghiep.exception.AppException;
import com.example.LuanVanTotNghiep.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ConvertService {

    public String convertToString(Cell cell) {
        if (cell == null) {
            return "";
        }
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue()).trim(); // Chuyển số thành chuỗi
            case BLANK -> "";
            default -> throw new AppException(ErrorCode.INVALID_INPUT);
        };
    }

    public boolean convertToBoolean(Cell cell) {
        if (cell == null) {
            return false;
        }
        return switch (cell.getCellType()) {
            case BOOLEAN -> cell.getBooleanCellValue();
            case NUMERIC -> cell.getNumericCellValue() != 0;
            case STRING -> Boolean.parseBoolean(cell.getStringCellValue().trim());
            default -> throw new AppException(ErrorCode.INVALID_INPUT);
        };
    }
}
