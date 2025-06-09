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
@Table(name = "universities")
public class Universities {
    @Id
    String university_id;
    @Column(name = "university_fullname")
    String universityfullname;
    @Column(name = "university_name")
    String universityname;
    String university_english_name;
    String content;

    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Branchs> branches = new HashSet<>();
}
