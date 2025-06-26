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
@Table(name = "highschools")
public class Highschools {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String high_id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    Users user;

    @ManyToOne
    @JoinColumn(name = "school_id", nullable = false)
    Schools school;

    @ManyToOne
    @JoinColumn(name = "province_id", nullable = false)
    Provinces province;

    String classyear;
}