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
@Table(name = "quantities")
public class Quantities {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String quantity_id;
    int admission_year;
    int quota;
    int current_applications;

    @ManyToMany(mappedBy = "quantities")
    Set<Branchs> branchs = new HashSet<>();
}
