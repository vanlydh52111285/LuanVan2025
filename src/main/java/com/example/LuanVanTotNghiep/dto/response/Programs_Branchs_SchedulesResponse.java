package com.example.LuanVanTotNghiep.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Programs_Branchs_SchedulesResponse {
    String pbs_id;
    SchedulesResponse schedulesResponse;
    Branchs_Entity_Response branchsEntityResponse;
    ProgramsResponse programsResponse;
}
