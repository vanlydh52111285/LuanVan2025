package com.example.LuanVanTotNghiep.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubjectScoreRequest {
    String sub_id;
    float average_score;
}
