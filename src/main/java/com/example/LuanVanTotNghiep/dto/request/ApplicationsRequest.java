package com.example.LuanVanTotNghiep.dto.request;

import com.example.LuanVanTotNghiep.enums.ApplicationEnum;
import com.example.LuanVanTotNghiep.enums.ApplicationTypeEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplicationsRequest {
    ApplicationTypeEnum application_type;
    ApplicationEnum status;
}
