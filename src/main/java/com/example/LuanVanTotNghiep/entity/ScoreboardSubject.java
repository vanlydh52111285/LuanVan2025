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
@Table(name = "scoreboards_subjects")
public class ScoreboardSubject {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "score_id", nullable = false)
    Scoreboards scoreboard;

    @ManyToOne
    @JoinColumn(name = "sub_id", nullable = false)
    Subjects subject;
    float average_score;
}
