package in.maven.ark.lms.course.service;

import in.maven.ark.lms.common.entity.Course;
import in.maven.ark.lms.common.entity.Module;
import in.maven.ark.lms.course.repository.CourseRepository;
import in.maven.ark.lms.course.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    public Course createCourse(Course course) {
        course.setStatus(Course.Status.DRAFT);
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, Course courseDetails) {
        Course course = getCourseById(id);
        
        course.setTitle(courseDetails.getTitle());
        course.setDescription(courseDetails.getDescription());
        course.setSlug(courseDetails.getSlug());
        course.setLevel(courseDetails.getLevel());
        course.setThumbnailUrl(courseDetails.getThumbnailUrl());
        course.setPreviewVideoUrl(courseDetails.getPreviewVideoUrl());
        course.setPrice(courseDetails.getPrice());
        course.setEstimatedHours(courseDetails.getEstimatedHours());
        course.setTags(courseDetails.getTags());
        
        return courseRepository.save(course);
    }

    public Course publishCourse(Long id) {
        Course course = getCourseById(id);
        course.setStatus(Course.Status.PUBLISHED);
        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        Course course = getCourseById(id);
        courseRepository.delete(course);
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }

    public Optional<Course> getCourseBySlug(String slug) {
        return courseRepository.findBySlug(slug);
    }

    public Page<Course> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    public Page<Course> getPublishedCourses(Pageable pageable) {
        return courseRepository.findByStatus(Course.Status.PUBLISHED, pageable);
    }

    public Page<Course> searchCourses(String keyword, Pageable pageable) {
        return courseRepository.searchCourses(keyword, pageable);
    }

    public List<Course> getCoursesByInstructor(Long instructorId) {
        return courseRepository.findByInstructorId(instructorId);
    }

    public Module addModuleToCourse(Long courseId, Module module) {
        Course course = getCourseById(courseId);
        module.setCourse(course);
        
        if (module.getOrderIndex() == null) {
            Integer maxOrder = moduleRepository.findMaxOrderIndexByCourseId(courseId);
            module.setOrderIndex(maxOrder != null ? maxOrder + 1 : 1);
        }
        
        return moduleRepository.save(module);
    }

    public Module updateModule(Long moduleId, Module moduleDetails) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found with id: " + moduleId));
        
        module.setTitle(moduleDetails.getTitle());
        module.setDescription(moduleDetails.getDescription());
        module.setOrderIndex(moduleDetails.getOrderIndex());
        
        return moduleRepository.save(module);
    }

    public void deleteModule(Long moduleId) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found with id: " + moduleId));
        moduleRepository.delete(module);
    }

    public List<Module> getCourseModules(Long courseId) {
        return moduleRepository.findByCourseIdOrderByOrderIndex(courseId);
    }
}
