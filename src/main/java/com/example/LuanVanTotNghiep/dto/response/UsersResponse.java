package com.example.LuanVanTotNghiep.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;


import java.util.Date;
import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UsersResponse {
    String user_id;
    String fullname;
    Date date_of_brith;
    String id_number;
    String phone_number;
    String email;
    String password;
    Date created_at;
    Set<String> role;
}
