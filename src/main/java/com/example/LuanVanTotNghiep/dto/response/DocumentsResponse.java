package com.example.LuanVanTotNghiep.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentsResponse {
    Integer documentId;
    String cccdUrl;
    String hocBaLop10Url;
    String hocBaLop11Url;
    String hocBaLop12Url;
    String bangTotNghiepThptUrl;
    String ketQuaThiThptUrl;
    String ketQuaThiDgnlUrl;
    String chungChiNgoaiNguUrl;
    String applicationId;
    String userId;
}
