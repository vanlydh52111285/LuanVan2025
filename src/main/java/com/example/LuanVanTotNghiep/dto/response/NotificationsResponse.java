package com.example.LuanVanTotNghiep.dto.response;

import com.example.LuanVanTotNghiep.enums.NotificationChanelEnum;
import com.example.LuanVanTotNghiep.enums.NotificationTypeEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationsResponse {
    String notification_id;
    String title;
    String content;
    Date send_date;
    NotificationChanelEnum chanel;
    NotificationTypeEnum type;
    boolean isRead;
}
