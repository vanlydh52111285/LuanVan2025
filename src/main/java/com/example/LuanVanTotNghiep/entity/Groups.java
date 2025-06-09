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
@Table(name = "groupss")
public class Groups {
    @Id
    String group_id;
    String content;
    @ManyToMany(mappedBy = "groups")
        Set<Branchs> branchs = new HashSet<>();
}
