package in.maven.ark.lms.git.service;

import in.maven.ark.lms.git.dto.GitRepositoryRequest;
import in.maven.ark.lms.git.dto.GitRepositoryResponse;
import in.maven.ark.lms.git.entity.GitRepository;
import in.maven.ark.lms.git.repository.GitRepositoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class GitIntegrationService {

    @Autowired
    private GitRepositoryRepository gitRepositoryRepository;

    @Autowired
    private GitHubService gitHubService;

    @Autowired
    private GitLabService gitLabService;

    @Value("${git.workspace.base-path}")
    private String workspaceBasePath;

    public GitRepositoryResponse createRepository(GitRepositoryRequest request) {
        GitRepository repository = new GitRepository();
        repository.setUserId(request.getUserId());
        repository.setProjectId(request.getProjectId());
        repository.setRepositoryUrl(request.getRepositoryUrl());
        repository.setRepositoryType(request.getRepositoryType());
        repository.setBranch(request.getBranch());
        repository.setAccessToken(request.getAccessToken());
        repository.setStatus(GitRepository.Status.ACTIVE);
        repository.setCreatedAt(LocalDateTime.now());

        // Clone repository locally
        String localPath = cloneRepository(repository);
        repository.setLocalPath(localPath);

        repository = gitRepositoryRepository.save(repository);

        return convertToResponse(repository);
    }

    public GitRepositoryResponse getRepository(Long repositoryId) {
        GitRepository repository = gitRepositoryRepository.findById(repositoryId)
                .orElseThrow(() -> new RuntimeException("Repository not found"));
        return convertToResponse(repository);
    }

    public GitRepositoryResponse getRepositoryByProject(Long projectId) {
        GitRepository repository = gitRepositoryRepository.findByProjectId(projectId)
                .orElseThrow(() -> new RuntimeException("Repository not found for project"));
        return convertToResponse(repository);
    }

    public GitRepositoryResponse updateRepository(Long repositoryId, GitRepositoryRequest request) {
        GitRepository repository = gitRepositoryRepository.findById(repositoryId)
                .orElseThrow(() -> new RuntimeException("Repository not found"));

        repository.setRepositoryUrl(request.getRepositoryUrl());
        repository.setBranch(request.getBranch());
        repository.setAccessToken(request.getAccessToken());
        repository.setUpdatedAt(LocalDateTime.now());

        repository = gitRepositoryRepository.save(repository);
        return convertToResponse(repository);
    }

    public void deleteRepository(Long repositoryId) {
        GitRepository repository = gitRepositoryRepository.findById(repositoryId)
                .orElseThrow(() -> new RuntimeException("Repository not found"));

        // Delete local repository
        deleteLocalRepository(repository.getLocalPath());

        gitRepositoryRepository.delete(repository);
    }

    public String pullRepository(Long repositoryId) {
        GitRepository repository = gitRepositoryRepository.findById(repositoryId)
                .orElseThrow(() -> new RuntimeException("Repository not found"));

        // Pull latest changes
        String commitHash = performPull(repository);
        
        repository.setLastSyncAt(LocalDateTime.now());
        repository.setLastCommitHash(commitHash);
        gitRepositoryRepository.save(repository);

        return commitHash;
    }

    public String createBranch(Long repositoryId, String branchName, String fromBranch) {
        GitRepository repository = gitRepositoryRepository.findById(repositoryId)
                .orElseThrow(() -> new RuntimeException("Repository not found"));

        // Create new branch
        String commitHash = performBranchCreation(repository, branchName, fromBranch);
        
        repository.setLastSyncAt(LocalDateTime.now());
        repository.setLastCommitHash(commitHash);
        gitRepositoryRepository.save(repository);

        return commitHash;
    }

    public String commitChanges(Long repositoryId, String message, String authorName, String authorEmail) {
        GitRepository repository = gitRepositoryRepository.findById(repositoryId)
                .orElseThrow(() -> new RuntimeException("Repository not found"));

        // Commit changes
        String commitHash = performCommit(repository, message, authorName, authorEmail);
        
        repository.setLastSyncAt(LocalDateTime.now());
        repository.setLastCommitHash(commitHash);
        gitRepositoryRepository.save(repository);

        return commitHash;
    }

    public void pushRepository(Long repositoryId, String branch) {
        GitRepository repository = gitRepositoryRepository.findById(repositoryId)
                .orElseThrow(() -> new RuntimeException("Repository not found"));

        // Push changes to remote
        performPush(repository, branch);
        
        repository.setLastSyncAt(LocalDateTime.now());
        gitRepositoryRepository.save(repository);
    }

    private String cloneRepository(GitRepository repository) {
        String workspaceId = UUID.randomUUID().toString();
        String localPath = workspaceBasePath + File.separator + workspaceId;
        
        // Create workspace directory
        new File(localPath).mkdirs();
        
        // Clone repository using JGit or git command
        // This is a simplified implementation
        try {
            // Implementation would use JGit to clone the repository
            // For now, return the path
            return localPath;
        } catch (Exception e) {
            throw new RuntimeException("Failed to clone repository", e);
        }
    }

    private void deleteLocalRepository(String localPath) {
        try {
            // Delete local repository directory
            File repoDir = new File(localPath);
            if (repoDir.exists()) {
                // Recursively delete directory
                deleteDirectory(repoDir);
            }
        } catch (Exception e) {
            // Log error but don't fail the operation
            System.err.println("Failed to delete local repository: " + e.getMessage());
        }
    }

    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }

    private String performPull(GitRepository repository) {
        // Implementation would use JGit to pull changes
        return "mock-commit-hash-" + System.currentTimeMillis();
    }

    private String performBranchCreation(GitRepository repository, String branchName, String fromBranch) {
        // Implementation would use JGit to create branch
        return "mock-commit-hash-" + System.currentTimeMillis();
    }

    private String performCommit(GitRepository repository, String message, String authorName, String authorEmail) {
        // Implementation would use JGit to commit changes
        return "mock-commit-hash-" + System.currentTimeMillis();
    }

    private void performPush(GitRepository repository, String branch) {
        // Implementation would use JGit to push changes
    }

    private GitRepositoryResponse convertToResponse(GitRepository repository) {
        GitRepositoryResponse response = new GitRepositoryResponse();
        response.setId(repository.getId());
        response.setUserId(repository.getUserId());
        response.setProjectId(repository.getProjectId());
        response.setRepositoryUrl(repository.getRepositoryUrl());
        response.setRepositoryType(repository.getRepositoryType());
        response.setBranch(repository.getBranch());
        response.setLocalPath(repository.getLocalPath());
        response.setStatus(repository.getStatus());
        response.setLastCommitHash(repository.getLastCommitHash());
        response.setLastSyncAt(repository.getLastSyncAt());
        response.setCreatedAt(repository.getCreatedAt());
        response.setUpdatedAt(repository.getUpdatedAt());
        return response;
    }
}
