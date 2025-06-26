package com.example.LuanVanTotNghiep.service;

import com.example.LuanVanTotNghiep.dto.request.UsersRequest;
import com.example.LuanVanTotNghiep.dto.request.VerifyRequest;
import com.example.LuanVanTotNghiep.dto.response.UsersResponse;
import com.example.LuanVanTotNghiep.entity.Districts;
import com.example.LuanVanTotNghiep.entity.Notifications;
import com.example.LuanVanTotNghiep.entity.Provinces;
import com.example.LuanVanTotNghiep.entity.Users;
import com.example.LuanVanTotNghiep.enums.Role;
import com.example.LuanVanTotNghiep.exception.AppException;
import com.example.LuanVanTotNghiep.exception.ErrorCode;
import com.example.LuanVanTotNghiep.mapper.UsersMapper;
import com.example.LuanVanTotNghiep.repository.DistrictsRepository;
import com.example.LuanVanTotNghiep.repository.ProvincesRepository;
import com.example.LuanVanTotNghiep.repository.UsersRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class UsersService {
    UsersRepository usersRepository;
    UsersMapper usersMapper;
    PasswordEncoder passwordEncoder;
    DistrictsRepository districtsRepository;
    ProvincesRepository provincesRepository;
    JavaMailSender javaMailSender;
    public int generateRandomFourDigitNumber() {
        Random random = new Random();
        // Sinh số ngẫu nhiên từ 1000 đến 9999
        return 1000 + random.nextInt(9000);
    }

    public void sendEmailOTP(Notifications notification, String email) {
        log.info("Sending email notification: id={}, title={}", notification.getNotification_id(), notification.getTitle());
        String recipient;

        // Xác định người nhận
        if (email == null) {
            recipient = "luanvantotnghiep2025@gmail.com"; // Email mặc định
            log.warn("No email request provided, using default recipient: {}", recipient);
        } else {
            recipient = email; // Lấy email từ emailRequest
            log.info("Using provided recipient: {}", recipient);
        }

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;

        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(recipient);
            helper.setSubject(notification.getTitle());
            helper.setText(notification.getContent(), true); // Hỗ trợ HTML



            javaMailSender.send(mimeMessage);
            log.info("Email sent successfully to {} for notification ID: {}", recipient, notification.getNotification_id());
        } catch (MessagingException e) {
            log.error("Failed to send email to {} for notification ID: {}", recipient, notification.getNotification_id(), e);
            throw new AppException(ErrorCode.UNEXPECTED_ERROR);
        }
    }

    public boolean Verify(VerifyRequest request){
        Users users = usersRepository.findByUsersId(request.getUser_id());
        boolean flag = false;
        if(users.getOtp().equals(request.getOtp())){
            flag = true;
            users.set_verify(true);
            usersRepository.save(users);
        }
        return flag;
    }
    public UsersResponse createStudent(UsersRequest request){
        if(usersRepository.existsByEmail(request.getEmail())){
            throw new AppException(ErrorCode.EMAIL_EXISTS);
        }
        Districts districts = districtsRepository.findBydistrictId(request.getDistrict_id());
        Provinces provinces = provincesRepository.findByProvinceId(request.getProvince_id());
        Users users = usersMapper.toCreateUser(request);
        users.setPassword(passwordEncoder.encode(request.getPassword()));
        users.setCreated_at(new Date(System.currentTimeMillis()));
        HashSet<String>roles = new HashSet<>();
        roles.add(Role.STUDENT.name());
        users.setRole(roles);
        users.set_verify(false);
        String otp = String.valueOf(generateRandomFourDigitNumber());
        users.setOtp(otp);
        users.setDistrict(districts);
        users.setProvince(provinces);
        Notifications notifications = Notifications.builder()
                .title("Mã xác thực ")
                .content("G-"+otp)
                .send_date(new Date(System.currentTimeMillis()))
                .build();
        sendEmailOTP(notifications,request.getEmail());
        return  usersMapper.toUserResponse(usersRepository.save(users));
    }
    public UsersResponse createCadre(UsersRequest request){
        if(usersRepository.existsByEmail(request.getEmail())){
            throw new AppException(ErrorCode.EMAIL_EXISTS);
        }
        Users users = usersMapper.toCreateUser(request);
        users.setPassword(passwordEncoder.encode(request.getPassword()));
        users.setCreated_at(new Date(System.currentTimeMillis()));
        HashSet<String>roles = new HashSet<>();
        roles.add(Role.CADRE.name());
        users.setRole(roles);
        return  usersMapper.toUserResponse(usersRepository.save(users));
    }
    public List<UsersResponse> getAllUsers(){
        List<UsersResponse> list = usersMapper.listUsers(usersRepository.findAll());
        return list;
    }
    @PostAuthorize("returnObject.user_id == authentication.name")
    public UsersResponse getUsers(String id){
        Users users = usersRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.UNAUTHORIZED));
        return usersMapper.toUserResponse(users);
    }

    public void deleteUsers(String id){
        usersRepository.deleteById(id);
    }

    public UsersResponse updateUsers(String id , UsersRequest request){
        // Tìm user theo ID
        Users users = usersRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND));

        // Kiểm tra province_id và district_id

            Provinces province = provincesRepository.findByProvinceId(request.getProvince_id());

            users.setProvince(province);


            Districts district = districtsRepository.findBydistrictId(request.getDistrict_id());
            users.setDistrict(district);


        // Cập nhật các trường khác từ request
        usersMapper.updateUser(users, request);
        users.setPassword(passwordEncoder.encode(request.getPassword()));
        // Lưu user đã cập nhật
        Users updatedUser = usersRepository.save(users);

        // Trả về response
        return usersMapper.toUserResponse(updatedUser);
    }
}
