package com.example.LuanVanTotNghiep.mapper;

import com.example.LuanVanTotNghiep.dto.request.NotificationsRequest;
import com.example.LuanVanTotNghiep.dto.response.NotificationsResponse;
import com.example.LuanVanTotNghiep.entity.Notifications;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationsMapper {
    Notifications toCreateNotification(NotificationsRequest request);
    NotificationsResponse toNotificationResponse(Notifications notification);
    void updateNotification(@MappingTarget Notifications notification, NotificationsRequest request);
    List<NotificationsResponse> listNotifications(List<Notifications> notificationsList);
}
