package com.example.LuanVanTotNghiep.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.validation.constraints.Size;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UsersRequest {
    String user_id;
    @Size (min = 2, message = "INVALID_USERNAME")
    String fullname;
    Date date_of_brith;
    String id_number;
    String phone_number;
    String email;
    @Size (min = 6, message = "INVALID_PASSWORD")
    String password;
}
