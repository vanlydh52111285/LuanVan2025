package com.example.LuanVanTotNghiep.dto.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupsRequest {
    String id;
    String group_id;
    String groupname;
    String sub1;
    String sub2;
    String sub3;
    boolean type;

}
