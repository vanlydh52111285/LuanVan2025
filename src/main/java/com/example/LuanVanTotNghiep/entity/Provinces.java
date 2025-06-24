package com.example.LuanVanTotNghiep.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "provinces")
public class Provinces {

    @Id
    String province_id; //mã vùng
    String province_name;

    @OneToMany(mappedBy = "district_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Districts> districts;

    @OneToMany(mappedBy = "school_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Schools> schools;
}
