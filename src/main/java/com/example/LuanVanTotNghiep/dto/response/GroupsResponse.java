package com.example.LuanVanTotNghiep.dto.response;

import com.example.LuanVanTotNghiep.entity.Branchs;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupsResponse {
    String group_id;
    String content;

}
