package com.example.LuanVanTotNghiep.dto.request;

import com.example.LuanVanTotNghiep.enums.NotificationChanelEnum;
import com.example.LuanVanTotNghiep.enums.NotificationTypeEnum;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CombinedNotificationRequest {
    String title;
    String content;
    NotificationChanelEnum chanel;
    NotificationTypeEnum type;
    boolean is_read;
    String singleEmail;
    List<String> emailList;
}
