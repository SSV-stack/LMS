package in.maven.ark.lms.ide.service;

import in.maven.ark.lms.ide.dto.IdeSessionRequest;
import in.maven.ark.lms.ide.dto.IdeSessionResponse;
import in.maven.ark.lms.ide.entity.IdeSession;
import in.maven.ark.lms.ide.repository.IdeSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class IdeIntegrationService {

    @Autowired
    private IdeSessionRepository ideSessionRepository;

    @Autowired
    private GoldIdeClient goldIdeClient;

    @Value("${gold-ide.session-timeout-minutes:120}")
    private int sessionTimeoutMinutes;

    public IdeSessionResponse createIdeSession(IdeSessionRequest request) {
        // Create session record
        IdeSession session = new IdeSession();
        session.setUserId(request.getUserId());
        session.setProjectId(request.getProjectId());
        session.setRepositoryUrl(request.getRepositoryUrl());
        session.setBranch(request.getBranch());
        session.setSessionToken(UUID.randomUUID().toString());
        session.setStatus(IdeSession.Status.ACTIVE);
        session.setCreatedAt(LocalDateTime.now());
        session.setExpiresAt(LocalDateTime.now().plusMinutes(sessionTimeoutMinutes));

        // Call Gold-IDE API to create workspace
        String ideSessionId = goldIdeClient.createWorkspace(request);
        session.setIdeSessionId(ideSessionId);

        // Save session
        session = ideSessionRepository.save(session);

        // Build response
        IdeSessionResponse response = new IdeSessionResponse();
        response.setSessionToken(session.getSessionToken());
        response.setIdeUrl(buildIdeUrl(ideSessionId));
        response.setExpiresAt(session.getExpiresAt());
        response.setWorkspaceConfig(request.getWorkspaceConfig());

        return response;
    }

    public IdeSession getSessionByToken(String sessionToken) {
        return ideSessionRepository.findBySessionToken(sessionToken)
                .orElseThrow(() -> new RuntimeException("Session not found with token: " + sessionToken));
    }

    public IdeSession validateSession(String sessionToken) {
        IdeSession session = getSessionByToken(sessionToken);
        
        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            session.setStatus(IdeSession.Status.EXPIRED);
            ideSessionRepository.save(session);
            throw new RuntimeException("Session has expired");
        }
        
        return session;
    }

    public void closeSession(String sessionToken) {
        IdeSession session = getSessionByToken(sessionToken);
        
        // Call Gold-IDE API to close workspace
        goldIdeClient.closeWorkspace(session.getIdeSessionId());
        
        // Update session status
        session.setStatus(IdeSession.Status.CLOSED);
        session.setClosedAt(LocalDateTime.now());
        ideSessionRepository.save(session);
    }

    public IdeSession extendSession(String sessionToken, int additionalMinutes) {
        IdeSession session = validateSession(sessionToken);
        
        // Extend session
        session.setExpiresAt(LocalDateTime.now().plusMinutes(additionalMinutes));
        session = ideSessionRepository.save(session);
        
        // Notify Gold-IDE about extension
        goldIdeClient.extendWorkspace(session.getIdeSessionId(), additionalMinutes);
        
        return session;
    }

    public void recordActivity(String sessionToken, String activityType, String metadata) {
        IdeSession session = validateSession(sessionToken);
        
        session.setLastActivityAt(LocalDateTime.now());
        session.setLastActivityType(activityType);
        session.setLastActivityMetadata(metadata);
        ideSessionRepository.save(session);
    }

    private String buildIdeUrl(String ideSessionId) {
        // This would construct the URL to open Gold-IDE with the specific session
        return String.format("%s/workspace/%s", 
            goldIdeClient.getGoldIdeBaseUrl(), ideSessionId);
    }
}
