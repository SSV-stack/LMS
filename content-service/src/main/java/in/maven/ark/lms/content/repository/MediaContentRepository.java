package in.maven.ark.lms.content.repository;

import in.maven.ark.lms.content.entity.MediaContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaContentRepository extends JpaRepository<MediaContent, Long> {
    
    List<MediaContent> findByLessonIdOrderByUploadedAtDesc(Long lessonId);
    
    List<MediaContent> findByContentTypeOrderByUploadedAtDesc(MediaContent.ContentType contentType);
    
    List<MediaContent> findByUploadedByOrderByUploadedAtDesc(Long uploadedBy);
    
    List<MediaContent> findByStatusOrderByUploadedAtDesc(MediaContent.Status status);
    
    Optional<MediaContent> findByStoredFileName(String storedFileName);
    
    @Query("SELECT m.contentType, COUNT(m) FROM MediaContent m GROUP BY m.contentType")
    List<Object[]> getContentTypeCounts();
    
    @Query("SELECT m.status, COUNT(m) FROM MediaContent m GROUP BY m.status")
    List<Object[]> getStatusCounts();
    
    @Query("SELECT SUM(m.fileSize) FROM MediaContent m WHERE m.contentType = :contentType")
    Long getTotalStorageByContentType(@Param("contentType") MediaContent.ContentType contentType);
    
    @Query("SELECT COUNT(m) FROM MediaContent m WHERE m.uploadedBy = :uploadedBy")
    long countByUploadedBy(@Param("uploadedBy") Long uploadedBy);
}
