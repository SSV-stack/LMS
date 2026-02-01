package in.maven.ark.lms.ide.repository;

import in.maven.ark.lms.ide.entity.IdeSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IdeSessionRepository extends JpaRepository<IdeSession, Long> {
    
    Optional<IdeSession> findBySessionToken(String sessionToken);
    
    Optional<IdeSession> findByIdeSessionId(String ideSessionId);
    
    List<IdeSession> findByUserId(Long userId);
    
    List<IdeSession> findByProjectId(Long projectId);
    
    @Query("SELECT s FROM IdeSession s WHERE s.status = :status AND s.expiresAt < :now")
    List<IdeSession> findExpiredSessions(@Param("status") IdeSession.Status status, @Param("now") LocalDateTime now);
    
    @Query("SELECT s FROM IdeSession s WHERE s.status = :status AND s.lastActivityAt < :cutoff")
    List<IdeSession> findInactiveSessions(@Param("status") IdeSession.Status status, @Param("cutoff") LocalDateTime cutoff);
    
    @Query("SELECT COUNT(s) FROM IdeSession s WHERE s.userId = :userId AND s.status = :status")
    long countActiveSessionsByUser(@Param("userId") Long userId, @Param("status") IdeSession.Status status);
}
