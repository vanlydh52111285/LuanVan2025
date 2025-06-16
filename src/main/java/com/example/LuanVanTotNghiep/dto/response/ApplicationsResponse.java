package com.example.LuanVanTotNghiep.dto.response;

import com.example.LuanVanTotNghiep.enums.ApplicationEnum;
import com.example.LuanVanTotNghiep.enums.ApplicationTypeEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplicationsResponse {
    String application_id;
    ApplicationTypeEnum application_type;
    ApplicationEnum status;
    Integer priority;
    Float total_score;
    Date create_date;
    Date update_date;
    String notes;
    String userId;
    String methodId;
}
