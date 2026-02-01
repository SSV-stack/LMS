package in.maven.ark.lms.course.repository;

import in.maven.ark.lms.common.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    Optional<Course> findBySlug(String slug);
    
    Page<Course> findByStatus(Course.Status status, Pageable pageable);
    
    Page<Course> findByLevel(Course.Level level, Pageable pageable);
    
    @Query("SELECT c FROM Course c WHERE " +
           "LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Course> searchCourses(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT c FROM Course c JOIN c.tags t WHERE t.name = :tagName")
    Page<Course> findByTag(@Param("tagName") String tagName, Pageable pageable);
    
    @Query("SELECT c FROM Course c WHERE c.instructorId = :instructorId")
    java.util.List<Course> findByInstructorId(@Param("instructorId") Long instructorId);
    
    @Query("SELECT COUNT(c) FROM Course c WHERE c.status = :status")
    long countByStatus(@Param("status") Course.Status status);
}
