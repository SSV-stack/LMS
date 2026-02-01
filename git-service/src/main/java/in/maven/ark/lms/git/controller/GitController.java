package in.maven.ark.lms.git.controller;

import in.maven.ark.lms.git.dto.GitRepositoryRequest;
import in.maven.ark.lms.git.dto.GitRepositoryResponse;
import in.maven.ark.lms.git.service.GitIntegrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/git")
public class GitController {

    @Autowired
    private GitIntegrationService gitIntegrationService;

    @PostMapping("/repositories")
    @PreAuthorize("hasRole('LEARNER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<GitRepositoryResponse> createRepository(@Valid @RequestBody GitRepositoryRequest request) {
        GitRepositoryResponse response = gitIntegrationService.createRepository(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/repositories/{repositoryId}")
    public ResponseEntity<GitRepositoryResponse> getRepository(@PathVariable Long repositoryId) {
        GitRepositoryResponse response = gitIntegrationService.getRepository(repositoryId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/repositories/project/{projectId}")
    public ResponseEntity<GitRepositoryResponse> getRepositoryByProject(@PathVariable Long projectId) {
        GitRepositoryResponse response = gitIntegrationService.getRepositoryByProject(projectId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/repositories/{repositoryId}")
    @PreAuthorize("hasRole('LEARNER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<GitRepositoryResponse> updateRepository(
            @PathVariable Long repositoryId, 
            @Valid @RequestBody GitRepositoryRequest request) {
        GitRepositoryResponse response = gitIntegrationService.updateRepository(repositoryId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/repositories/{repositoryId}")
    @PreAuthorize("hasRole('LEARNER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRepository(@PathVariable Long repositoryId) {
        gitIntegrationService.deleteRepository(repositoryId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/repositories/{repositoryId}/pull")
    @PreAuthorize("hasRole('LEARNER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<String> pullRepository(@PathVariable Long repositoryId) {
        String commitHash = gitIntegrationService.pullRepository(repositoryId);
        return ResponseEntity.ok(commitHash);
    }

    @PostMapping("/repositories/{repositoryId}/branch")
    @PreAuthorize("hasRole('LEARNER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<String> createBranch(
            @PathVariable Long repositoryId,
            @RequestParam String branchName,
            @RequestParam(required = false, defaultValue = "main") String fromBranch) {
        String commitHash = gitIntegrationService.createBranch(repositoryId, branchName, fromBranch);
        return ResponseEntity.ok(commitHash);
    }

    @PostMapping("/repositories/{repositoryId}/commit")
    @PreAuthorize("hasRole('LEARNER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<String> commitChanges(
            @PathVariable Long repositoryId,
            @RequestParam String message,
            @RequestParam String authorName,
            @RequestParam String authorEmail) {
        String commitHash = gitIntegrationService.commitChanges(repositoryId, message, authorName, authorEmail);
        return ResponseEntity.ok(commitHash);
    }

    @PostMapping("/repositories/{repositoryId}/push")
    @PreAuthorize("hasRole('LEARNER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Void> pushRepository(
            @PathVariable Long repositoryId,
            @RequestParam(required = false, defaultValue = "main") String branch) {
        gitIntegrationService.pushRepository(repositoryId, branch);
        return ResponseEntity.ok().build();
    }
}
