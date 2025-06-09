package com.example.LuanVanTotNghiep.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "schedules")
public class Schedules {
    @Id
            @GeneratedValue(strategy = GenerationType.UUID)
    String schedule_id;
    String schedule_name;
    Date star_time;
    Date end_time;
    int quota;
    int current_applications;

}
