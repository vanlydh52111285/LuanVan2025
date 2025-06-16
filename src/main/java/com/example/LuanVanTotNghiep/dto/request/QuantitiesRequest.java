package com.example.LuanVanTotNghiep.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuantitiesRequest {
    String quantity_id;
    int admission_year;
    int quota;
    int current_applications;
    String branch_id;
}
