package com.example.LuanVanTotNghiep.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DistrictsRequest {
    String id;
    String district_id;
    String district_name;
    String province_id;
}
