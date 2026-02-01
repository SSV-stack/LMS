package in.maven.ark.lms.ide.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class IdeSessionResponse {
    
    private String sessionToken;
    private String ideUrl;
    private LocalDateTime expiresAt;
    private Map<String, Object> workspaceConfig;

    public IdeSessionResponse() {}

    public IdeSessionResponse(String sessionToken, String ideUrl, LocalDateTime expiresAt) {
        this.sessionToken = sessionToken;
        this.ideUrl = ideUrl;
        this.expiresAt = expiresAt;
    }

    public String getSessionToken() { return sessionToken; }
    public void setSessionToken(String sessionToken) { this.sessionToken = sessionToken; }

    public String getIdeUrl() { return ideUrl; }
    public void setIdeUrl(String ideUrl) { this.ideUrl = ideUrl; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public Map<String, Object> getWorkspaceConfig() { return workspaceConfig; }
    public void setWorkspaceConfig(Map<String, Object> workspaceConfig) { this.workspaceConfig = workspaceConfig; }
}
