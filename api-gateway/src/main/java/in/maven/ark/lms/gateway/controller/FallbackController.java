package in.maven.ark.lms.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/auth")
    public Mono<Map<String, Object>> authFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Authentication service is temporarily unavailable");
        response.put("service", "auth-service");
        response.put("timestamp", System.currentTimeMillis());
        return Mono.just(response);
    }

    @GetMapping("/courses")
    public Mono<Map<String, Object>> coursesFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Course service is temporarily unavailable");
        response.put("service", "course-service");
        response.put("timestamp", System.currentTimeMillis());
        return Mono.just(response);
    }

    @GetMapping("/ide")
    public Mono<Map<String, Object>> ideFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "IDE service is temporarily unavailable");
        response.put("service", "ide-service");
        response.put("timestamp", System.currentTimeMillis());
        return Mono.just(response);
    }

    @GetMapping("/content")
    public Mono<Map<String, Object>> contentFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Content service is temporarily unavailable");
        response.put("service", "content-service");
        response.put("timestamp", System.currentTimeMillis());
        return Mono.just(response);
    }

    @GetMapping("/git")
    public Mono<Map<String, Object>> gitFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Git service is temporarily unavailable");
        response.put("service", "git-service");
        response.put("timestamp", System.currentTimeMillis());
        return Mono.just(response);
    }

    @GetMapping("/ai")
    public Mono<Map<String, Object>> aiFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "AI service is temporarily unavailable");
        response.put("service", "ai-service");
        response.put("timestamp", System.currentTimeMillis());
        return Mono.just(response);
    }

    @GetMapping("/notifications")
    public Mono<Map<String, Object>> notificationsFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Notification service is temporarily unavailable");
        response.put("service", "notification-service");
        response.put("timestamp", System.currentTimeMillis());
        return Mono.just(response);
    }
}
