package com.example.LuanVanTotNghiep.service;

import com.example.LuanVanTotNghiep.dto.request.EmailRequest;
import com.example.LuanVanTotNghiep.dto.request.NotificationsRequest;
import com.example.LuanVanTotNghiep.dto.response.NotificationsResponse;
import com.example.LuanVanTotNghiep.entity.Notifications;
import com.example.LuanVanTotNghiep.enums.NotificationChanelEnum;
import com.example.LuanVanTotNghiep.exception.AppException;
import com.example.LuanVanTotNghiep.exception.ErrorCode;
import com.example.LuanVanTotNghiep.mapper.NotificationsMapper;
import com.example.LuanVanTotNghiep.repository.NotificationsRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class NotificationsService {
    public NotificationsRepository notificationsRepository;
    NotificationsMapper notificationsMapper;
    JavaMailSender javaMailSender;

    @Transactional
    public NotificationsResponse createNotification(NotificationsRequest request, EmailRequest emailRequest) {
        log.info("Creating notification with request: {}, emailRequest: {}", request, emailRequest);
        // Kiểm tra dữ liệu đầu vào
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new AppException(ErrorCode.REQUEST_IS_EMPTY);
        }
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new AppException(ErrorCode.REQUEST_IS_EMPTY);
        }
        if (request.getChanel() == null) {
            throw new AppException(ErrorCode.REQUEST_IS_EMPTY);
        }
        if (request.getType() == null) {
            throw new AppException(ErrorCode.REQUEST_IS_EMPTY);
        }

        Notifications notification = notificationsMapper.toCreateNotification(request);
        notification.setSend_date(new Date());
        notification.setRead(false); // Sửa từ set_read thành setIs_read

        Notifications saved = notificationsRepository.save(notification);
        log.info("Created notification: id={}, title={}", saved.getNotification_id(), saved.getTitle());

        // Gửi thông báo nếu kênh là EMAIL
        if (notification.getChanel() == NotificationChanelEnum.EMAIL) {
            sendEmailNotification(saved, emailRequest);
        }

        return notificationsMapper.toNotificationResponse(saved);
    }

    @Transactional
    public void deleteNotification(String notificationId) {
        log.info("Deleting notification with ID: {}", notificationId);
        Notifications notification = notificationsRepository.findById(notificationId)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));

        notificationsRepository.delete(notification);
        log.info("Deleted notification with ID: {}", notificationId);
    }

    @Transactional
    public void sendEmailNotification(Notifications notification, EmailRequest emailRequest) {
        log.info("Sending email notification: id={}, title={}", notification.getNotification_id(), notification.getTitle());
        List<String> recipients;

        // Xác định danh sách người nhận
        if (emailRequest == null || (emailRequest.getSingleEmail() == null || emailRequest.getSingleEmail().trim().isEmpty())
                && (emailRequest.getEmailList() == null || emailRequest.getEmailList().isEmpty())) {
            throw new AppException(ErrorCode.REQUEST_IS_EMPTY);
        }

        if (emailRequest.getSingleEmail() != null && !emailRequest.getSingleEmail().trim().isEmpty()) {
            recipients = List.of(emailRequest.getSingleEmail()); // Gửi cho một người dùng
        } else if (emailRequest.getEmailList() != null && !emailRequest.getEmailList().isEmpty()) {
            recipients = emailRequest.getEmailList(); // Gửi hàng loạt
        } else {
            recipients = List.of("luanvantotnghiep2025@gmail.com"); // Thay bằng logic lấy email thực tế
            log.warn("No email request provided, using default recipient: {}", recipients);
        }

        for (String recipient : recipients) {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper;

            try {
                helper = new MimeMessageHelper(mimeMessage, true);
                helper.setTo(recipient);
                helper.setSubject(notification.getTitle());
                helper.setText(notification.getContent(), true); // true để hỗ trợ HTML

                // Thêm link để đánh dấu đã đọc
                String readLink = "http://localhost:8082/notifications/read/" + notification.getNotification_id();
                String htmlContent = notification.getContent() + "<br><a href='" + readLink + "'>Đánh dấu đã đọc để tránh việc gửi lại thông báo</a>";
                helper.setText(htmlContent, true);

                javaMailSender.send(mimeMessage);
                log.info("Email sent successfully to {} for notification ID: {}", recipient, notification.getNotification_id());
            } catch (MessagingException e) {
                log.error("Failed to send email to {} for notification ID: {}", recipient, notification.getNotification_id(), e);
                throw new AppException(ErrorCode.UNEXPECTED_ERROR);
            }
        }
    }

    @Transactional
    public void markNotificationAsRead(String notificationId) {
        log.info("Marking notification as read with ID: {}", notificationId);
        Notifications notification = notificationsRepository.findById(notificationId)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));

        notification.setRead(true);
        notificationsRepository.save(notification);
        log.info("Marked notification as read: id={}, is_read={}", notification.getNotification_id(), notification.isRead());
    }
}
