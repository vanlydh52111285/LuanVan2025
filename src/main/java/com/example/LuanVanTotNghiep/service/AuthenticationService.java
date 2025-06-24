package com.example.LuanVanTotNghiep.service;

import com.example.LuanVanTotNghiep.dto.request.AuthenticationRequest;
import com.example.LuanVanTotNghiep.dto.request.IntrospectRequest;
import com.example.LuanVanTotNghiep.dto.response.AuthenticationResponse;
import com.example.LuanVanTotNghiep.dto.response.IntrospectResponse;
import com.example.LuanVanTotNghiep.entity.Users;
import com.example.LuanVanTotNghiep.exception.AppException;
import com.example.LuanVanTotNghiep.exception.ErrorCode;
import com.example.LuanVanTotNghiep.repository.UsersRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import org.springframework.security.crypto.password.PasswordEncoder;

// Đánh dấu lớp là một Spring Service, được quản lý bởi Spring IoC container
@Slf4j // Tạo logger từ SLF4J để ghi log lỗi hoặc thông tin
@Service // Xác định đây là một service trong Spring
@RequiredArgsConstructor // Tạo constructor tự động cho các trường final (usersReponsitory)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Đặt tất cả các trường là private và final, trừ @NonFinal
public class AuthenticationService {
    // Repository để truy vấn dữ liệu người dùng từ database
    UsersRepository usersReponsitory; // Lưu ý: Có lỗi chính tả "usersReponsitory", nên là "usersRepository"
    UsersRepository usersRepository;

    // Khóa bí mật để ký và xác minh JWT, lấy từ file cấu hình (application.properties)
    @NonFinal // Cho phép gán giá trị cho trường này
    @Value("${jwt.signerKey}") // Lấy giá trị từ thuộc tính jwt.signerKey
    protected String KEY; // Khóa dùng cho thuật toán HS512

    public IntrospectResponse introspect (IntrospectRequest request) throws ParseException, JOSEException {
        var token=request.getToken();

        JWSVerifier verifier=new MACVerifier(KEY.getBytes());
        SignedJWT signedJWT=SignedJWT.parse(token);
        Date expiryTime=signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);
        return IntrospectResponse.builder()
                .valid( verified && expiryTime.after(new Date()))
                .build();

    }

    // Xử lý yêu cầu đăng nhập và trả về JWT
    public AuthenticationResponse authenticationResponse(AuthenticationRequest request){
        // Tìm người dùng theo email, ném ngoại lệ nếu không tìm thấy
        var user=usersReponsitory.findByEmail(request.getEmail()).orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND));
        // Lưu ý: ErrorCode.USERNAME_NOINVALD có thể không chính xác, nên dùng USER_NOT_FOUND hoặc tương tự

        // Tạo BCryptPasswordEncoder với strength=5 để kiểm tra mật khẩu
        PasswordEncoder passwordEncoder =new BCryptPasswordEncoder(5);
        // Lưu ý: Strength=5 thấp, dễ bị tấn công; nên tăng lên 10 hoặc dùng bean PasswordEncoder được tiêm

        // So sánh mật khẩu đầu vào với mật khẩu đã mã hóa trong database
        boolean auth= passwordEncoder.matches(request.getPassword(),user.getPassword());

        // Ném ngoại lệ nếu mật khẩu không khớp
        if(!auth){
            throw new AppException(ErrorCode.INVALID_PASSWORD_LOGIN);
        }

        // Tạo JWT cho người dùng
        var token=generToken(user);

        // Trả về AuthenticationResponse chứa JWT và trạng thái xác thực
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build(); // Sử dụng Lombok @Builder để tạo đối tượng
    }

    // Tạo JWT cho người dùng
    String generToken (Users users){
        // Tạo header với thuật toán HMAC SHA-512
        JWSHeader header=new JWSHeader(JWSAlgorithm.HS512);

        // Tạo payload với các claims
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(users.getUser_id())
                .issuer(users.getEmail())
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli()))
                .claim("scope", buidScope(users))
                .build();

        // Chuyển payload thành JSON
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        // Tạo đối tượng JWS từ header và payload
        JWSObject jwsObject = new JWSObject(header,payload);

        try {
            // Ký JWS với khóa bí mật
            jwsObject.sign(new MACSigner(KEY.getBytes()));
            // Trả về chuỗi JWT (header.payload.signature)
            return jwsObject.serialize();
        }catch (JOSEException e){
            // Ghi log lỗi và ném ngoại lệ nếu ký thất bại
            log.error("Cannot create token",e);
            throw new RuntimeException(e);
        }
    }

    // Xử lý chuỗi yêu cầu từ người dùng
    String buidScope(Users users){
        // Tạo StringJoiner để nối các role bằng dấu cách
        StringJoiner stringJoiner = new StringJoiner(" ");

        // Kiểm tra nếu danh sách role không rỗng
        if(!CollectionUtils.isEmpty(users.getRole()))
            // Thêm từng role vào chuỗi
            users.getRole().forEach(stringJoiner::add);

        // Trả về chuỗi scope (ví dụ: "ROLE_USER ROLE_ADMIN")
        return stringJoiner.toString();
        // Lưu ý: Lỗi chính tả "buidScope", nên là "buildScope"
    }

    public String getAuthenticatedUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        String user_id = authentication.getName(); // Lấy user_id từ token (sub)
        Users user = usersRepository.findById(user_id)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        return user.getUser_id();

    }
}