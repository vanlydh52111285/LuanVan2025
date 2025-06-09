package com.example.LuanVanTotNghiep.dto.response;

import com.example.LuanVanTotNghiep.entity.Branchs;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UniversitiesResponse {
    String university_id;
    String universityfullname;
    String universityname;
    String university_english_name;
    String content;
    Set<Branchs> branches = new HashSet<>();
}
