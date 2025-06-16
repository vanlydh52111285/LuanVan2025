package com.example.LuanVanTotNghiep.dto.response;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuantitiesResponse {
    String quantity_id;
    int admission_year;
    int quota;
    int current_applications;
    Set<Branchs_QuantitiesResponse> branchsQuantities;
}
