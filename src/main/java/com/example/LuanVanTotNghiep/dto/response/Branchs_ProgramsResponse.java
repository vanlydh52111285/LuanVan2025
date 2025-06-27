package com.example.LuanVanTotNghiep.dto.response;

import com.example.LuanVanTotNghiep.entity.Programs;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Branchs_ProgramsResponse {
    String branch_id;
    String branchname;
    boolean status;
    Set<Programs> programs;
}
