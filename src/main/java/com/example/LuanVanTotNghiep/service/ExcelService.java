package com.example.LuanVanTotNghiep.service;

import com.example.LuanVanTotNghiep.dto.response.UsersResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ExcelService {

    public ByteArrayInputStream exportUsersToExcel(List<UsersResponse> users) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Users");

            // Tạo header
            Row headerRow = sheet.createRow(0);
            String[] columns = {"User ID", "Full Name", "Date of Birth", "ID Number", "Phone Number", "Email", "Password", "Created At", "Roles"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Định dạng ngày
            CellStyle dateCellStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));

            // Điền dữ liệu
            int rowIdx = 1;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            for (UsersResponse user : users) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(user.getUser_id());
                row.createCell(1).setCellValue(user.getFullname());

                // Định dạng Date of Birth
                Cell dobCell = row.createCell(2);
                if (user.getDate_of_brith() != null) {
                    dobCell.setCellValue(user.getDate_of_brith());
                    dobCell.setCellStyle(dateCellStyle);
                } else {
                    dobCell.setCellValue("");
                }

                row.createCell(3).setCellValue(user.getId_number());
                row.createCell(4).setCellValue(user.getPhone_number());
                row.createCell(5).setCellValue(user.getEmail());
                row.createCell(6).setCellValue(user.getPassword());

                // Định dạng Created At
                Cell createdAtCell = row.createCell(7);
                if (user.getCreated_at() != null) {
                    createdAtCell.setCellValue(user.getCreated_at());
                    createdAtCell.setCellStyle(dateCellStyle);
                } else {
                    createdAtCell.setCellValue("");
                }

                // Chuyển Set<String> role thành chuỗi
                String roles = user.getRole() != null ? user.getRole().stream().collect(Collectors.joining(", ")) : "";
                row.createCell(8).setCellValue(roles);
            }

            // Auto-size cột
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Ghi workbook vào output stream
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            log.error("Failed to export data to Excel", e);
            throw new RuntimeException("Failed to export data to Excel", e);
        }
    }
}