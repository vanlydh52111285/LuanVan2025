package com.example.LuanVanTotNghiep.controller;

import com.example.LuanVanTotNghiep.dto.response.UsersResponse;
import com.example.LuanVanTotNghiep.service.ExcelService;
import com.example.LuanVanTotNghiep.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@Slf4j
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @Autowired
    private UsersService usersService;

    @GetMapping(value = "/api/excel/download", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<Resource> downloadExcel() {
        log.info("Received request to download Excel file");

        // Lấy danh sách users từ UsersService
        List<UsersResponse> users = usersService.getAllUsers();
        log.info("Fetched {} users from database", users.size());

        // Kiểm tra danh sách rỗng
        if (users.isEmpty()) {
            log.warn("No users found to export");
            return ResponseEntity.noContent().build();
        }

        // Tạo file Excel
        ByteArrayInputStream in = excelService.exportUsersToExcel(users);
        log.info("Excel file generated successfully");

        // Thiết lập header để tải file
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.xlsx");
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.EXPIRES, "0");

        // Trả về file
        InputStreamResource resource = new InputStreamResource(in);

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);
    }
}