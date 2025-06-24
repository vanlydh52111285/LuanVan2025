package com.example.LuanVanTotNghiep.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubjectsRequest {

    String sub_id;
    String sub_name;
    boolean sub_sgk_2006;
    boolean sub_sgk_2018;
}
