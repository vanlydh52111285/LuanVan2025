package com.example.LuanVanTotNghiep.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Programs_Branchs_SchedulesRequest {
    String pbs_id;
    String program_id;
    String branch_id;
    String schedule_id;
}
