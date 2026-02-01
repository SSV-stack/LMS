package in.maven.ark.lms.git.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "git_repositories")
public class GitRepository {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "project_id")
    private Long projectId;
    
    @Column(name = "repository_url")
    private String repositoryUrl;
    
    @Enumerated(EnumType.STRING)
    private RepositoryType repositoryType;
    
    private String branch;
    
    @Column(name = "access_token")
    private String accessToken;
    
    @Column(name = "local_path")
    private String localPath;
    
    @Column(name = "last_commit_hash")
    private String lastCommitHash;
    
    @Column(name = "last_sync_at")
    private LocalDateTime lastSyncAt;
    
    @Enumerated(EnumType.STRING)
    private Status status;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum RepositoryType {
        GITHUB, GITLAB, BITBUCKET, CUSTOM
    }
    
    public enum Status {
        ACTIVE, INACTIVE, ERROR, SYNCING
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
    
    public RepositoryType getRepositoryType() { return repositoryType; }
    public void setRepositoryType(RepositoryType repositoryType) { this.repositoryType = repositoryType; }
    
    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }
    
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    
    public String getLocalPath() { return localPath; }
    public void setLocalPath(String localPath) { this.localPath = localPath; }
    
    public String getLastCommitHash() { return lastCommitHash; }
    public void setLastCommitHash(String lastCommitHash) { this.lastCommitHash = lastCommitHash; }
    
    public LocalDateTime getLastSyncAt() { return lastSyncAt; }
    public void setLastSyncAt(LocalDateTime lastSyncAt) { this.lastSyncAt = lastSyncAt; }
    
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
