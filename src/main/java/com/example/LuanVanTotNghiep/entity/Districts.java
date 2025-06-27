package com.example.LuanVanTotNghiep.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "districts")
public class Districts {

    @Id
    String district_id;
    String district_name;

    @ManyToOne
    @JoinColumn(name = "province_id", nullable = false)
    Provinces province;
}
