package com.example.LuanVanTotNghiep.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    // Lỗi chung
    //UNEXPECTED_ERROR(1000, "Lỗi không xác định", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_REQUEST(1001, "Yêu cầu không hợp lệ", HttpStatus.BAD_REQUEST),
    UNEXPECTED_ERROR(1017, "Lỗi không xác định", HttpStatus.INTERNAL_SERVER_ERROR),

    // Lỗi liên quan đến xác thực
    UNAUTHORIZED(1002, "Không được phép truy cập", HttpStatus.UNAUTHORIZED),
    INVALID_CREDENTIALS(1003, "Thông tin đăng nhập không hợp lệ", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(1004, "Token đã hết hạn", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(1005, "Token không hợp lệ", HttpStatus.UNAUTHORIZED),
    INVALID_STATE(2002, "Trạng thái không hợp lệ", HttpStatus.BAD_REQUEST),

    // Lỗi liên quan đến người dùng
    USERNAME_EXISTS(1006, "Tên người dùng đã tồn tại", HttpStatus.BAD_REQUEST),
    USERNAME_NOT_FOUND(1007, "Tên người dùng không tồn tại", HttpStatus.NOT_FOUND),
    INVALID_USERNAME(1008, "Tên người dùng không hợp lệ (ít nhất 3 ký tự)", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1009, "Mật khẩu không hợp lệ (ít nhất 6 ký tự)", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTS(1010, "Email đã tồn tại", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(1011, "Email không hợp lệ", HttpStatus.BAD_REQUEST),
    CONTENT_EXISTS(1017, "Tổ hợp đã tồn tại", HttpStatus.BAD_REQUEST),
    CONTENT_NO_EXISTS(1018, "Tổ hợp không tồn tại", HttpStatus.BAD_REQUEST),
    UNIVERSITYFULLNAME_EXISTS(1019, "Tên trường đã tồn tại", HttpStatus.BAD_REQUEST),
    UNIVERSITYFULLNAME_NOT_FOUND(1020,"Tên trường không tồn tại", HttpStatus.NOT_FOUND),
    UNIVERSITYNAME_EXISTS(1021, "Tên viết tắt trường đã tồn tại", HttpStatus.BAD_REQUEST),
    UNIVERSITYNAME_NOT_FOUND(1022, "Tên viết tắt trường không tồn tại", HttpStatus.NOT_FOUND),
    UNIVERSITYID_EXISTS(1023, "Mã trường đã tồn tại", HttpStatus.BAD_REQUEST),
    UNIVERSITYID_NOT_FOUND(1024, "Mã trường dùng không tồn tại", HttpStatus.NOT_FOUND),
    BRANCHID_EXISTS(1025, "Mã hệ ngành đã tồn tại", HttpStatus.BAD_REQUEST),
    BRANCHID_NOT_FOUND(1026, "Mã hệ ngành không tồn tại", HttpStatus.NOT_FOUND),
    BRANCHNAME_EXISTS(1027, "Tên hệ ngành đã tồn tại", HttpStatus.BAD_REQUEST),
    BRANCHNAME_NOT_FOUND(1028, "Tên hệ ngành không tồn tại", HttpStatus.NOT_FOUND),
    NO_GROUPS_REMOVED(1029,"Không có tổ nào được xóa",HttpStatus.NOT_FOUND),
    PROGRAMID_EXISTS(1030, "Mã chương trình đã tồn tại", HttpStatus.BAD_REQUEST),
    PROGRAMID_NOT_FOUND(1031, "Mã chương trình không tồn tại", HttpStatus.NOT_FOUND),
    PROGRAMNAME_EXISTS(1032, "Tên chương trình đã tồn tại", HttpStatus.BAD_REQUEST),
    PROGRAMNAME_NOT_FOUND(1033, "Tên chương trình không tồn tại", HttpStatus.NOT_FOUND),
    QUANTITY_EXISTS(1034, "Ngành tuyển theo năm đã tồn tại", HttpStatus.BAD_REQUEST),
    QUANTITY_NOT_FOUND(1035, "Ngành tuyển theo năm không tồn tại", HttpStatus.NOT_FOUND),
    SCHEDULE_EXISTS(1036, "Lịch tuyển đã tồn tại", HttpStatus.BAD_REQUEST),
    SCHEDULE_NOT_FOUND(1037, "Lịch tuyển không tồn tại", HttpStatus.NOT_FOUND),

    // Lỗi liên quan đến dữ liệu
    DATA_NOT_FOUND(1012, "Dữ liệu không được tìm thấy", HttpStatus.NOT_FOUND),
    INVALID_DATA_FORMAT(1013, "Định dạng dữ liệu không hợp lệ", HttpStatus.BAD_REQUEST),
    DATA_CONFLICT(1014, "Dữ liệu bị xung đột", HttpStatus.CONFLICT),
    DATA_ALREADY_EXISTS(2001, "Dữ liệu đã tồn tại", HttpStatus.BAD_REQUEST),

    INVALID_TIME_FORMAT(1038,"Thời gian tuyển không hợp lệ",HttpStatus.INTERNAL_SERVER_ERROR),
    // Lỗi liên quan đến cơ sở dữ liệu
    DATABASE_ERROR(1015, "Lỗi cơ sở dữ liệu", HttpStatus.INTERNAL_SERVER_ERROR),
    CONSTRAINT_VIOLATION(1016, "Vi phạm ràng buộc dữ liệu", HttpStatus.BAD_REQUEST);

    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    final int code;
    final String message;
    final HttpStatus statusCode;
}