package in.maven.ark.lms.ai.repository;

import in.maven.ark.lms.ai.entity.AiInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AiInteractionRepository extends JpaRepository<AiInteraction, Long> {
    
    List<AiInteraction> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    List<AiInteraction> findByCourseIdOrderByCreatedAtDesc(Long courseId);
    
    List<AiInteraction> findByProjectIdOrderByCreatedAtDesc(Long projectId);
    
    List<AiInteraction> findByInteractionTypeOrderByCreatedAtDesc(AiInteraction.InteractionType interactionType);
    
    List<AiInteraction> findByStatusOrderByCreatedAtDesc(AiInteraction.Status status);
    
    long countByStatus(AiInteraction.Status status);
    
    long countByInteractionType(AiInteraction.InteractionType interactionType);
    
    @Query("SELECT COUNT(a) FROM AiInteraction a WHERE a.createdAt >= :since")
    long countInteractionsSince(@Param("since") LocalDateTime since);
    
    @Query("SELECT a.interactionType, COUNT(a) FROM AiInteraction a GROUP BY a.interactionType")
    List<Object[]> getInteractionTypeCounts();
    
    @Query("SELECT a.status, COUNT(a) FROM AiInteraction a GROUP BY a.status")
    List<Object[]> getStatusCounts();
}
