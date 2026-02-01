package in.maven.ark.lms.git.dto;

import in.maven.ark.lms.git.entity.GitRepository;
import java.time.LocalDateTime;

public class GitRepositoryResponse {
    
    private Long id;
    private Long userId;
    private Long projectId;
    private String repositoryUrl;
    private GitRepository.RepositoryType repositoryType;
    private String branch;
    private String localPath;
    private GitRepository.Status status;
    private String lastCommitHash;
    private LocalDateTime lastSyncAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public GitRepositoryResponse() {}

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }

    public String getRepositoryUrl() { return repositoryUrl; }
    public void setRepositoryUrl(String repositoryUrl) { this.repositoryUrl = repositoryUrl; }

    public GitRepository.RepositoryType getRepositoryType() { return repositoryType; }
    public void setRepositoryType(GitRepository.RepositoryType repositoryType) { this.repositoryType = repositoryType; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public String getLocalPath() { return localPath; }
    public void setLocalPath(String localPath) { this.localPath = localPath; }

    public GitRepository.Status getStatus() { return status; }
    public void setStatus(GitRepository.Status status) { this.status = status; }

    public String getLastCommitHash() { return lastCommitHash; }
    public void setLastCommitHash(String lastCommitHash) { this.lastCommitHash = lastCommitHash; }

    public LocalDateTime getLastSyncAt() { return lastSyncAt; }
    public void setLastSyncAt(LocalDateTime lastSyncAt) { this.lastSyncAt = lastSyncAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
