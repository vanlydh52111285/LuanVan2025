package com.example.LuanVanTotNghiep.configuration;

import com.example.LuanVanTotNghiep.entity.Districts;
import com.example.LuanVanTotNghiep.entity.Provinces;
import com.example.LuanVanTotNghiep.entity.Users;
import com.example.LuanVanTotNghiep.enums.Role;
import com.example.LuanVanTotNghiep.exception.AppException;
import com.example.LuanVanTotNghiep.exception.ErrorCode;
import com.example.LuanVanTotNghiep.repository.DistrictsRepository;
import com.example.LuanVanTotNghiep.repository.ProvincesRepository;
import com.example.LuanVanTotNghiep.repository.UsersRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.HashSet;

// Đánh dấu lớp là một cấu hình Spring, được sử dụng để thiết lập các bean hoặc logic khởi tạo
@Configuration
// Tạo constructor tự động cho các trường final (passwordEncoder)
@RequiredArgsConstructor
// Đặt tất cả các trường là private và final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
// Tạo logger từ SLF4J để ghi log
@Slf4j
public class ApplicationInitConfig {


    // Bean PasswordEncoder để mã hóa mật khẩu (thường là BCryptPasswordEncoder)
    PasswordEncoder passwordEncoder;
    DistrictsRepository districtsRepository;
    ProvincesRepository provincesRepository;
    // Tạo bean ApplicationRunner để chạy logic khởi tạo khi ứng dụng khởi động
    @Bean
    ApplicationRunner applicationRunner(UsersRepository usersReponsitory) {
        // Lưu ý: Lỗi chính tả "usersReponsitory", nên là "usersRepository"
        return args -> {
            // Kiểm tra xem người dùng với full_name là "admin" đã tồn tại chưa
            if (usersReponsitory.findByFullname("admin").isEmpty()) {
                // Tạo tập hợp roles và thêm vai trò ADMIN từ enum Role
                var roles = new HashSet<String>();
                roles.add(Role.ADMIN.name()); // Role.ADMIN.name() trả về chuỗi "ADMIN"
                Districts districts = districtsRepository.findBydistrictId("01_001")
                        .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND, "Không tìm thấy quận/huyện với district_id: 01_001"));
                Provinces provinces = provincesRepository.findByProvinceId("01")
                        .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND, "Không tìm thấy tỉnh/thành với province_id: 01"));

                // Tạo đối tượng Users với thông tin admin
                Users users = Users.builder()
                        .fullname("admin") // Đặt tên đầy đủ là "admin"
                        .email("admin@gmail.com") // Đặt email là "admin@gmail.com"
                        .password(passwordEncoder.encode("admin")) // Mã hóa mật khẩu "admin"
                        .role(roles) // Gán tập hợp roles chứa "ADMIN"
                        .created_at(new Date(System.currentTimeMillis()))
                        .date_of_brith(new Date(System.currentTimeMillis()))
                        .id_number("1")
                        .phone_number("0123456789")
                        .district(districts)
                        .province(provinces)
                        .build(); // Sử dụng Lombok @Builder để tạo đối tượng

                // Lưu người dùng admin vào database thông qua UsersRepository
                usersReponsitory.save(users);

                // Ghi log cảnh báo rằng tài khoản admin đã được tạo với mật khẩu mặc định
                log.warn("admin user has been create with defautl password: admin, please change it");
                // Lưu ý: Lỗi chính tả "create" nên là "created", "defautl" nên là "default"
            }
        };
    }
}