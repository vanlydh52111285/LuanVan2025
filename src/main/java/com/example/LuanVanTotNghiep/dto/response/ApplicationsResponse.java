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
    String applicationId;
    ApplicationTypeEnum applicationType;
    ApplicationEnum status;
    Integer priority;
    Float totalScore;
    Date createDate;
    Date updateDate;
    String notes;
    String userId;
    Integer methodId;
}
