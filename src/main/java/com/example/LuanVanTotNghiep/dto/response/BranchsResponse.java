package com.example.LuanVanTotNghiep.dto.response;

import com.example.LuanVanTotNghiep.entity.Groups;
import com.example.LuanVanTotNghiep.entity.Universities;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BranchsResponse {
    String branch_id;
    String branchname;
    UniversitiesResponse universityResponse;

}
