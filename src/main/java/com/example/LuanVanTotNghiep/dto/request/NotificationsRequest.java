package com.example.LuanVanTotNghiep.dto.request;

import com.example.LuanVanTotNghiep.enums.NotificationChanelEnum;
import com.example.LuanVanTotNghiep.enums.NotificationTypeEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationsRequest {
    String title;
    String content;
    NotificationChanelEnum chanel;
    NotificationTypeEnum type;
    boolean isRead;
}
