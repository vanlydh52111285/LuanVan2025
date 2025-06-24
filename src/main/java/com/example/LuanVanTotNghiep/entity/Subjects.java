package com.example.LuanVanTotNghiep.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "subjects")
public class Subjects {

    @Id
    String sub_id;
    String sub_name;
    boolean sub_sgk_2006;
    boolean sub_sgk_2018;
}
