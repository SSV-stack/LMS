package in.maven.ark.lms.ide.controller;

import in.maven.ark.lms.ide.dto.IdeSessionRequest;
import in.maven.ark.lms.ide.dto.IdeSessionResponse;
import in.maven.ark.lms.ide.entity.IdeSession;
import in.maven.ark.lms.ide.service.IdeIntegrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ide")
public class IdeController {

    @Autowired
    private IdeIntegrationService ideIntegrationService;

    @PostMapping("/session")
    @PreAuthorize("hasRole('LEARNER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<IdeSessionResponse> createIdeSession(@Valid @RequestBody IdeSessionRequest request) {
        IdeSessionResponse response = ideIntegrationService.createIdeSession(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/session/{sessionToken}")
    public ResponseEntity<IdeSession> getSession(@PathVariable String sessionToken) {
        IdeSession session = ideIntegrationService.getSessionByToken(sessionToken);
        return ResponseEntity.ok(session);
    }

    @PostMapping("/session/{sessionToken}/validate")
    public ResponseEntity<IdeSession> validateSession(@PathVariable String sessionToken) {
        IdeSession session = ideIntegrationService.validateSession(sessionToken);
        return ResponseEntity.ok(session);
    }

    @PostMapping("/session/{sessionToken}/close")
    @PreAuthorize("hasRole('LEARNER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Void> closeSession(@PathVariable String sessionToken) {
        ideIntegrationService.closeSession(sessionToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/session/{sessionToken}/extend")
    @PreAuthorize("hasRole('LEARNER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<IdeSession> extendSession(
            @PathVariable String sessionToken,
            @RequestParam(defaultValue = "30") int additionalMinutes) {
        IdeSession session = ideIntegrationService.extendSession(sessionToken, additionalMinutes);
        return ResponseEntity.ok(session);
    }

    @PostMapping("/session/{sessionToken}/activity")
    @PreAuthorize("hasRole('LEARNER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Void> recordActivity(
            @PathVariable String sessionToken,
            @RequestParam String activityType,
            @RequestParam(required = false) String metadata) {
        ideIntegrationService.recordActivity(sessionToken, activityType, metadata);
        return ResponseEntity.ok().build();
    }
}
