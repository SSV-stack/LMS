package in.maven.ark.lms.notification.service;

import in.maven.ark.lms.notification.dto.*;
import in.maven.ark.lms.notification.entity.Notification;
import in.maven.ark.lms.notification.entity.NotificationTemplate;
import in.maven.ark.lms.notification.repository.NotificationRepository;
import in.maven.ark.lms.notification.repository.NotificationTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationTemplateRepository templateRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private WebSocketService webSocketService;

    @Value("${notification.email.enabled:true}")
    private boolean emailEnabled;

    @Value("${notification.websocket.enabled:true}")
    private boolean webSocketEnabled;

    public NotificationResponse sendNotification(NotificationRequest request) {
        // Create notification entity
        Notification notification = new Notification();
        notification.setUserId(request.getUserId());
        notification.setType(request.getType());
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setActionUrl(request.getActionUrl());
        notification.setPriority(request.getPriority());
        notification.setStatus(Notification.Status.PENDING);
        notification.setCreatedAt(LocalDateTime.now());

        // Save notification
        notification = notificationRepository.save(notification);

        // Send via different channels
        if (emailEnabled && request.getChannels().contains(Notification.Channel.EMAIL)) {
            sendEmailNotification(notification);
        }

        if (webSocketEnabled && request.getChannels().contains(Notification.Channel.WEBSOCKET)) {
            sendWebSocketNotification(notification);
        }

        // Update status
        notification.setStatus(Notification.Status.SENT);
        notification.setSentAt(LocalDateTime.now());
        notification = notificationRepository.save(notification);

        return convertToResponse(notification);
    }

    @Async
    public void sendBulkNotifications(BulkNotificationRequest request) {
        for (Long userId : request.getUserIds()) {
            NotificationRequest notificationRequest = new NotificationRequest();
            notificationRequest.setUserId(userId);
            notificationRequest.setType(request.getType());
            notificationRequest.setTitle(request.getTitle());
            notificationRequest.setMessage(request.getMessage());
            notificationRequest.setActionUrl(request.getActionUrl());
            notificationRequest.setPriority(request.getPriority());
            notificationRequest.setChannels(request.getChannels());

            sendNotification(notificationRequest);
        }
    }

    public NotificationResponse sendTemplateNotification(TemplateNotificationRequest request) {
        // Find template
        NotificationTemplate template = templateRepository.findByTypeAndLanguage(
            request.getTemplateType(), request.getLanguage())
            .orElseThrow(() -> new RuntimeException("Template not found"));

        // Process template with variables
        String processedTitle = processTemplate(template.getTitleTemplate(), request.getVariables());
        String processedMessage = processTemplate(template.getMessageTemplate(), request.getVariables());

        // Create notification request
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setUserId(request.getUserId());
        notificationRequest.setType(template.getType());
        notificationRequest.setTitle(processedTitle);
        notificationRequest.setMessage(processedMessage);
        notificationRequest.setActionUrl(request.getActionUrl());
        notificationRequest.setPriority(template.getDefaultPriority());
        notificationRequest.setChannels(template.getDefaultChannels());

        return sendNotification(notificationRequest);
    }

    public List<NotificationResponse> getUserNotifications(Long userId, Notification.Status status) {
        List<Notification> notifications;
        if (status != null) {
            notifications = notificationRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, status);
        } else {
            notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        }
        
        return notifications.stream()
            .map(this::convertToResponse)
            .toList();
    }

    public NotificationResponse markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        
        notification.setStatus(Notification.Status.READ);
        notification.setReadAt(LocalDateTime.now());
        notification = notificationRepository.save(notification);
        
        return convertToResponse(notification);
    }

    public void markAllAsRead(Long userId) {
        List<Notification> unreadNotifications = notificationRepository
            .findByUserIdAndStatusOrderByCreatedAtDesc(userId, Notification.Status.SENT);
        
        for (Notification notification : unreadNotifications) {
            notification.setStatus(Notification.Status.READ);
            notification.setReadAt(LocalDateTime.now());
        }
        
        notificationRepository.saveAll(unreadNotifications);
    }

    public void deleteNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notificationRepository.delete(notification);
    }

    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndStatus(userId, Notification.Status.SENT);
    }

    public NotificationTemplate createTemplate(NotificationTemplate template) {
        template.setCreatedAt(LocalDateTime.now());
        return templateRepository.save(template);
    }

    public List<NotificationTemplate> getTemplates() {
        return templateRepository.findAll();
    }

    private void sendEmailNotification(Notification notification) {
        try {
            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setToUserId(notification.getUserId());
            emailRequest.setSubject(notification.getTitle());
            emailRequest.setBody(notification.getMessage());
            emailRequest.setActionUrl(notification.getActionUrl());
            
            emailService.sendEmail(emailRequest);
        } catch (Exception e) {
            // Log error but don't fail the notification
            System.err.println("Failed to send email notification: " + e.getMessage());
        }
    }

    private void sendWebSocketNotification(Notification notification) {
        try {
            webSocketService.sendNotification(notification.getUserId(), notification);
        } catch (Exception e) {
            // Log error but don't fail the notification
            System.err.println("Failed to send WebSocket notification: " + e.getMessage());
        }
    }

    private String processTemplate(String template, Map<String, Object> variables) {
        String result = template;
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            result = result.replace("{{" + entry.getKey() + "}}", String.valueOf(entry.getValue()));
        }
        return result;
    }

    private NotificationResponse convertToResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setUserId(notification.getUserId());
        response.setType(notification.getType());
        response.setTitle(notification.getTitle());
        response.setMessage(notification.getMessage());
        response.setActionUrl(notification.getActionUrl());
        response.setPriority(notification.getPriority());
        response.setStatus(notification.getStatus());
        response.setCreatedAt(notification.getCreatedAt());
        response.setSentAt(notification.getSentAt());
        response.setReadAt(notification.getReadAt());
        return response;
    }
}
