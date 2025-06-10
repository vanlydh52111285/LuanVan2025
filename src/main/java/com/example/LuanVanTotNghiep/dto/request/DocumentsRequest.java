package com.example.LuanVanTotNghiep.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentsRequest {
    String applicationId;
    String cccdUrl;
    String hocBaLop10Url;
    String hocBaLop11Url;
    String hocBaLop12Url;
    String bangTotNghiepThptUrl;
    String ketQuaThiThptUrl;
    String ketQuaThiDgnlUrl;
    String chungChiNgoaiNguUrl;
}
