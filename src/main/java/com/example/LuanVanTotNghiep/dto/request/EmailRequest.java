package com.example.LuanVanTotNghiep.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailRequest {


    private String singleEmail; // Email cụ thể cho gửi đơn lẻ
    private List<String> emailList; // Danh sách email cho gửi hàng loạt
}