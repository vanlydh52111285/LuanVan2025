package com.example.LuanVanTotNghiep.entity;

import com.example.LuanVanTotNghiep.dto.response.Branchs_QuantitiesResponse;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "quantities")
public class Quantities {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String quantity_id;
    int admission_year;
    int quota;
    int current_applications;
    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "branchs_quantities", // Tên bảng trung gian
            joinColumns = @JoinColumn(name = "quantity_id"), // Khóa ngoại tới Branchs
            inverseJoinColumns = @JoinColumn(name = "branch_id") // Khóa ngoại tới Quantities
    )
    Set<Branchs> branchs = new HashSet<>();
    // Tùy chỉnh hashCode và equals

}
