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
@Table(name = "branchs")
public class Branchs {
    @Id
    String branch_id;
    @Column(name = "branch_name")
    String branchname;
    boolean status;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "branchs_groups",
            joinColumns = @JoinColumn(name = "branch_id"), // Khóa chính của Branchs
            inverseJoinColumns = @JoinColumn(name = "group_id") // Khóa chính của Groups
    )
    Set<Groups> groups = new HashSet<>();


    @ManyToOne
    @JoinColumn(name = "university_id", nullable = false)
    Universities university;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "branchs_programs", // Tên bảng trung gian
            joinColumns = @JoinColumn(name = "branch_id"), // Khóa ngoại tới Programs
            inverseJoinColumns = @JoinColumn(name = "program_id") // Khóa ngoại tới Branchs
    ) // Liên kết ngược lại với Programs
    Set<Programs> programs = new HashSet<>();
    @Override
    public int hashCode() {
        return Objects.hash(branch_id, branchname); // Chỉ sử dụng branch_id và branchname
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Branchs branchs = (Branchs) o;
        return Objects.equals(branch_id, branchs.branch_id) &&
                Objects.equals(branchname, branchs.branchname);
    }
}
