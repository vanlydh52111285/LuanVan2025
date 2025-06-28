package com.example.LuanVanTotNghiep.dto.response;

import com.example.LuanVanTotNghiep.dto.request.SubjectScoreRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScoreboardsResponse {
    float average_score_10;
    float average_score_11;
    float average_score_12;
    float score_dgnl;
    String application_id;
    List<SubjectScoreRequest> subjects;
}
