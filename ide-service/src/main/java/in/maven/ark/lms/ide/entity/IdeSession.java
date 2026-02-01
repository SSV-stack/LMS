package in.maven.ark.lms.ide.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ide_sessions")
public class IdeSession {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "project_id")
    private Long projectId;
    
    @Column(name = "repository_url")
    private String repositoryUrl;
    
    private String branch;
    
    @Column(name = "session_token", unique = true)
    private String sessionToken;
    
    @Column(name = "ide_session_id")
    private String ideSessionId;
    
    @Enumerated(EnumType.STRING)
    private Status status;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    
    @Column(name = "closed_at")
    private LocalDateTime closedAt;
    
    @Column(name = "last_activity_at")
    private LocalDateTime lastActivityAt;
    
    @Column(name = "last_activity_type")
    private String lastActivityType;
    
    @Column(name = "last_activity_metadata", columnDefinition = "TEXT")
    private String lastActivityMetadata;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    public enum Status {
        ACTIVE, EXPIRED, CLOSED, TERMINATED
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    
    public String getRepositoryUrl() { return repositoryUrl; }
    public void setRepositoryUrl(String repositoryUrl) { this.repositoryUrl = repositoryUrl; }
    
    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }
    
    public String getSessionToken() { return sessionToken; }
    public void setSessionToken(String sessionToken) { this.sessionToken = sessionToken; }
    
    public String getIdeSessionId() { return ideSessionId; }
    public void setIdeSessionId(String ideSessionId) { this.ideSessionId = ideSessionId; }
    
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    
    public LocalDateTime getClosedAt() { return closedAt; }
    public void setClosedAt(LocalDateTime closedAt) { this.closedAt = closedAt; }
    
    public LocalDateTime getLastActivityAt() { return lastActivityAt; }
    public void setLastActivityAt(LocalDateTime lastActivityAt) { this.lastActivityAt = lastActivityAt; }
    
    public String getLastActivityType() { return lastActivityType; }
    public void setLastActivityType(String lastActivityType) { this.lastActivityType = lastActivityType; }
    
    public String getLastActivityMetadata() { return lastActivityMetadata; }
    public void setLastActivityMetadata(String lastActivityMetadata) { this.lastActivityMetadata = lastActivityMetadata; }
}
