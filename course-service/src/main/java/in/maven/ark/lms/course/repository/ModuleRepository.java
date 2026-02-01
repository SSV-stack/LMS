package in.maven.ark.lms.course.repository;

import in.maven.ark.lms.common.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    
    List<Module> findByCourseIdOrderByOrderIndex(Long courseId);
    
    @Query("SELECT COALESCE(MAX(m.orderIndex), 0) FROM Module m WHERE m.courseId = :courseId")
    Integer findMaxOrderIndexByCourseId(@Param("courseId") Long courseId);
    
    @Query("SELECT COUNT(m) FROM Module m WHERE m.courseId = :courseId")
    long countByCourseId(@Param("courseId") Long courseId);
}
