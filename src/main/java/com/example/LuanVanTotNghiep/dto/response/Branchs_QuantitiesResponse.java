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
public class Branchs_QuantitiesResponse {
    String branch_id;
    String branchname;
}
