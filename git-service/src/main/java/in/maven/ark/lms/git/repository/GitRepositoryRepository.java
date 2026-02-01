package in.maven.ark.lms.git.repository;

import in.maven.ark.lms.git.entity.GitRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GitRepositoryRepository extends JpaRepository<GitRepository, Long> {
    
    Optional<GitRepository> findByProjectId(Long projectId);
    
    List<GitRepository> findByUserId(Long userId);
    
    List<GitRepository> findByStatus(GitRepository.Status status);
    
    Optional<GitRepository> findByRepositoryUrlAndBranch(String repositoryUrl, String branch);
    
    List<GitRepository> findByRepositoryType(GitRepository.RepositoryType repositoryType);
}
