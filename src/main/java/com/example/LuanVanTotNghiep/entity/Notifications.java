package com.example.LuanVanTotNghiep.entity;

import com.example.LuanVanTotNghiep.enums.NotificationChanelEnum;
import com.example.LuanVanTotNghiep.enums.NotificationTypeEnum;
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
@Table(name = "notifications")
public class Notifications {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String notification_id;
    String title;
    String content;
    Date send_date;

    @Enumerated(EnumType.STRING)
    NotificationChanelEnum chanel;

    @Enumerated(EnumType.STRING)
    NotificationTypeEnum type;

    @Column(name = "is_read")
    boolean isRead;
}
