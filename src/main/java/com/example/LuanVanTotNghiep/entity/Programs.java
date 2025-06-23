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
@Table(name = "programs")
public class Programs {
    @Id
    String program_id;
    @Column(name = "program_name")
    String programname;
    boolean type;
}
