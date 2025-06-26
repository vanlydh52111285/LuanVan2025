package com.example.LuanVanTotNghiep.dto.response;

import com.example.LuanVanTotNghiep.enums.MethodEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MethodResponse {
    String method_id;
    String method_name;
    MethodEnum status;
}
