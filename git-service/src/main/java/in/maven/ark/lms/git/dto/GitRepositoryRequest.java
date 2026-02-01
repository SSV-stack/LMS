package in.maven.ark.lms.git.dto;

import in.maven.ark.lms.git.entity.GitRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class GitRepositoryRequest {
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Project ID is required")
    private Long projectId;
    
    @NotBlank(message = "Repository URL is required")
    private String repositoryUrl;
    
    @NotNull(message = "Repository type is required")
    private GitRepository.RepositoryType repositoryType;
    
    @NotBlank(message = "Branch is required")
    private String branch;
    
    private String accessToken;

    public GitRepositoryRequest() {}

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

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
}
