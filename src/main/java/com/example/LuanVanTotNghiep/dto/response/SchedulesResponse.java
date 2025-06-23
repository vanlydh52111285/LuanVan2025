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
public class SchedulesResponse {
    String schedule_id;
    String schedulename;
    Date star_time;
    Date end_time;
}
