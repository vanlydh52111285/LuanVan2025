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
    String cccd;
    String hoc_ba_lop10;
    String hoc_ba_lop11;
    String hoc_ba_lop12;
    String bang_tot_nghiep_thpt;
    String ket_qua_thi_thpt;
    String ket_qua_thi_dgnl;
    String chung_chi_ngoai_ngu;
    String applicationId;
    String userId;
}
