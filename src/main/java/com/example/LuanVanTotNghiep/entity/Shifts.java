package com.example.LuanVanTotNghiep.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "shifts")
public class Shifts {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String shift_id;
    Date work_date;
    LocalTime start_time;
    LocalTime end_time;
    @ManyToMany
    @JoinTable(
            name = "users_shifts",
            joinColumns = @JoinColumn(name = "shift_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    Set<Users> users = new HashSet<>();
}
