package com.example.LuanVanTotNghiep.controller;

import com.example.LuanVanTotNghiep.dto.request.CombinedNotificationRequest;
import com.example.LuanVanTotNghiep.dto.request.EmailRequest;
import com.example.LuanVanTotNghiep.dto.request.NotificationsRequest;
import com.example.LuanVanTotNghiep.dto.response.ApiResponse;
import com.example.LuanVanTotNghiep.dto.response.NotificationsResponse;
import com.example.LuanVanTotNghiep.entity.Notifications;
import com.example.LuanVanTotNghiep.exception.AppException;
import com.example.LuanVanTotNghiep.exception.ErrorCode;
import com.example.LuanVanTotNghiep.service.NotificationsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationsController {
    NotificationsService notificationsService;

    @PostMapping("/create")
    public ApiResponse<NotificationsResponse> createNotification(@RequestBody CombinedNotificationRequest request) {
        NotificationsRequest notificationRequest = new NotificationsRequest();
        notificationRequest.setTitle(request.getTitle());
        notificationRequest.setContent(request.getContent());
        notificationRequest.setChanel(request.getChanel());
        notificationRequest.setType(request.getType());
        notificationRequest.setRead(request.is_read());

        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setSingleEmail(request.getSingleEmail());
        emailRequest.setEmailList(request.getEmailList());

        NotificationsResponse result = notificationsService.createNotification(notificationRequest, emailRequest);
        return ApiResponse.<NotificationsResponse>builder()
                .code(1000)
                .message("Tạo thông báo thành công")
                .result(result)
                .build();
    }

    @DeleteMapping("/delete/{notification_id}")
    public ApiResponse<String> deleteNotification(@PathVariable String notification_id) {
        notificationsService.deleteNotification(notification_id);
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Xóa thông báo thành công")
                .result("Notification with ID " + notification_id + " deleted successfully")
                .build();
    }

    @PutMapping("/read/{notification_id}")
    public ApiResponse<String> markNotificationAsRead(@PathVariable String notification_id) {
        notificationsService.markNotificationAsRead(notification_id);
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Đánh dấu thông báo đã đọc thành công")
                .result("Notification with ID " + notification_id + " marked as read")
                .build();
    }

    @PostMapping("/send-email/{notification_id}")
    public ApiResponse<String> sendEmailNotification(@PathVariable String notification_id, @RequestBody EmailRequest emailRequest) {
        Notifications notification = notificationsService.notificationsRepository.findById(notification_id)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        notificationsService.sendEmailNotification(notification, emailRequest);
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Gửi email thành công")
                .result("Email sent for notification ID " + notification_id)
                .build();
    }

    @GetMapping("/mark-read/{notification_id}")
    public void markReadAutomatically(@PathVariable String notification_id) {
        notificationsService.markNotificationAsRead(notification_id);
    }
}
