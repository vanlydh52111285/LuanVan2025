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
@Table(name = "aspirations")
public class Aspirations {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String aspiration_id;
    int priority_order;
    // Thêm mối quan hệ N-1 với Branchs
    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    Branchs branch;
}
