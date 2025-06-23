package com.example.LuanVanTotNghiep.entity;

import com.example.LuanVanTotNghiep.enums.MethodEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "methods")
public class Methods {

    @Id
    String method_id;
    String method_name;

    @Enumerated(EnumType.STRING)
    MethodEnum status;

    @OneToMany(mappedBy = "admission_method", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Applications> applicationList;

    @ManyToOne
    @JoinColumn(name = "university_id")
    Universities university;
}
