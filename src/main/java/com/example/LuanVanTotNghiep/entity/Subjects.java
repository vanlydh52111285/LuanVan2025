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
@Table(name = "subjects")
public class Subjects {

    @Id
    String sub_id;
    String sub_name;
    boolean sub_sgk_2006;
    boolean sub_sgk_2018;

    @OneToMany(mappedBy = "subject")
    Set<ScoreboardSubject> scoreboardSubjects = new HashSet<>();
}
