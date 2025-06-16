package com.example.LuanVanTotNghiep.entity;

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
    @Override
    public int hashCode() {
        return Objects.hash(university_id, universityname); // Chỉ sử dụng university_id và university_name
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Universities that = (Universities) o;
        return Objects.equals(university_id, that.university_id) &&
                Objects.equals(universityname, that.universityname);
    }
}
