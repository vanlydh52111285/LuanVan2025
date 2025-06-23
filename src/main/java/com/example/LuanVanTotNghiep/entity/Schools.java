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
@Table(name = "schools")
public class Schools {

    @Id
    String school_id;
    String school_name;

    @ManyToOne
    @JoinColumn(name = "province_id", nullable = false)
    Provinces province;
}
