package com.example.LuanVanTotNghiep.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentsResponse {
    Integer document_id;
    String document_link_cccd;
    String document_link_hoc_ba_lop10;
    String document_link_hoc_ba_lop11;
    String document_link_hoc_ba_lop12;
    String document_link_bang_tot_nghiep_thpt;
    String document_link_ket_qua_thi_thpt;
    String document_link_ket_qua_thi_dgnl;
    String document_link_chung_chi_ngoai_ngu;
    String applicationId;
    String userId;
}
