package in.maven.ark.lms.notification.controller;

import in.maven.ark.lms.notification.dto.*;
import in.maven.ark.lms.notification.entity.Notification;
import in.maven.ark.lms.notification.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<NotificationResponse> sendNotification(@Valid @RequestBody NotificationRequest request) {
        NotificationResponse response = notificationService.sendNotification(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-bulk")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> sendBulkNotifications(@Valid @RequestBody BulkNotificationRequest request) {
        notificationService.sendBulkNotifications(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send-template")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<NotificationResponse> sendTemplateNotification(@Valid @RequestBody TemplateNotificationRequest request) {
        NotificationResponse response = notificationService.sendTemplateNotification(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('LEARNER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<NotificationResponse>> getUserNotifications(
            @PathVariable Long userId,
            @RequestParam(required = false) Notification.Status status) {
        List<NotificationResponse> notifications = notificationService.getUserNotifications(userId, status);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/{notificationId}/read")
    @PreAuthorize("hasRole('LEARNER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<NotificationResponse> markAsRead(@PathVariable Long notificationId) {
        NotificationResponse response = notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/user/{userId}/read-all")
    @PreAuthorize("hasRole('LEARNER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Void> markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}/unread-count")
    @PreAuthorize("hasRole('LEARNER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Long> getUnreadCount(@PathVariable Long userId) {
        long count = notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(count);
    }

    @DeleteMapping("/{notificationId}")
    @PreAuthorize("hasRole('LEARNER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.noContent().build();
    }
}
