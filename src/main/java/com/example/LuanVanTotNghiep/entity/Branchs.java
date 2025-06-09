package com.example.LuanVanTotNghiep.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "branchs")
public class Branchs {
    @Id
    String branch_id;
    String branch_name;
    Set<String> branch_type;
    @ManyToMany
    @JoinTable(
            name = "branchs_groups", // Tên bảng trung gian
            joinColumns = @JoinColumn(name = "group_id"), // Khóa ngoại tới Groups
            inverseJoinColumns = @JoinColumn(name = "branch_id") // Khóa ngoại tới Branchs
    )
    Set<Groups> groups = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "branchs_quantities", // Tên bảng trung gian
            joinColumns = @JoinColumn(name = "branch_id"), // Khóa ngoại tới Branchs
            inverseJoinColumns = @JoinColumn(name = "quantity_id") // Khóa ngoại tới Quantities
    )
    Set<Quantities> quantities = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "university_id", nullable = false)
    Universities university;
}
