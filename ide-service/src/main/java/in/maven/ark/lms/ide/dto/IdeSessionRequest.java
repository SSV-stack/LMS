package in.maven.ark.lms.ide.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public class IdeSessionRequest {
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Project ID is required")
    private Long projectId;
    
    @NotBlank(message = "Repository URL is required")
    private String repositoryUrl;
    
    @NotBlank(message = "Branch is required")
    private String branch;
    
    private Map<String, Object> workspaceConfig;

    public IdeSessionRequest() {}

    public IdeSessionRequest(Long userId, Long projectId, String repositoryUrl, String branch) {
        this.userId = userId;
        this.projectId = projectId;
        this.repositoryUrl = repositoryUrl;
        this.branch = branch;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }

    public String getRepositoryUrl() { return repositoryUrl; }
    public void setRepositoryUrl(String repositoryUrl) { this.repositoryUrl = repositoryUrl; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public Map<String, Object> getWorkspaceConfig() { return workspaceConfig; }
    public void setWorkspaceConfig(Map<String, Object> workspaceConfig) { this.workspaceConfig = workspaceConfig; }
}
