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
@Table(name = "scoreboards")
public class Scoreboards {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String score_id;
    float average_score_10;
    float average_score_11;
    float average_score_12;
    float score_dgnl;

    @OneToOne
    @JoinColumn(name = "application_id")
    Applications applications;

    @OneToMany(mappedBy = "scoreboard", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<ScoreboardSubject> scoreboardSubjects = new HashSet<>();


}
