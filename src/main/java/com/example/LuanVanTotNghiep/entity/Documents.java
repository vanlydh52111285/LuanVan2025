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
@Table(name = "documents")
public class Documents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int document_id;
    String cccd;
    String hoc_ba_lop10;
    String hoc_ba_lop11;
    String hoc_ba_lop12;
    String bang_tot_nghiep_thpt;
    String ket_qua_thi_thpt;
    String ket_qua_thi_dgnl;
    String chung_chi_ngoai_ngu;

    @OneToOne
    @JoinColumn(name = "application_id", nullable = true)
    Applications application;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    Users user;

}
