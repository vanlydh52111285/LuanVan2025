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
    String document_link_cccd;
    String document_link_hoc_ba_lop10;
    String document_link_hoc_ba_lop11;
    String document_link_hoc_ba_lop12;
    String document_link_bang_tot_nghiep_thpt;
    String document_link_ket_qua_thi_thpt;
    String document_link_ket_qua_thi_dgnl;
    String document_link_chung_chi_ngoai_ngu;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = true)
    Applications application;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    Users user;

}
