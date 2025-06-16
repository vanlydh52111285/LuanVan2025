package com.example.LuanVanTotNghiep.dto.request;

import com.example.LuanVanTotNghiep.enums.MethodEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MethodsRequest {

    @Positive(message = "ID phương thức phải là số dương")
    String method_id;

    @NotBlank(message = "Tên phương thức không được để trống")
    String method_name;
    MethodEnum status;
    String application_id;
}
