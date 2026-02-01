package in.maven.ark.lms.course.controller;

import in.maven.ark.lms.common.entity.Course;
import in.maven.ark.lms.common.entity.Module;
import in.maven.ark.lms.course.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Course> createCourse(@Valid @RequestBody Course course) {
        Course createdCourse = courseService.createCourse(course);
        return ResponseEntity.ok(createdCourse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourse(@PathVariable Long id) {
        Course course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<Course> getCourseBySlug(@PathVariable String slug) {
        return courseService.getCourseBySlug(slug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @Valid @RequestBody Course course) {
        Course updatedCourse = courseService.updateCourse(id, course);
        return ResponseEntity.ok(updatedCourse);
    }

    @PostMapping("/{id}/publish")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Course> publishCourse(@PathVariable Long id) {
        Course publishedCourse = courseService.publishCourse(id);
        return ResponseEntity.ok(publishedCourse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<Course>> getAllCourses(Pageable pageable) {
        Page<Course> courses = courseService.getAllCourses(pageable);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/published")
    public ResponseEntity<Page<Course>> getPublishedCourses(Pageable pageable) {
        Page<Course> courses = courseService.getPublishedCourses(pageable);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Course>> searchCourses(@RequestParam String keyword, Pageable pageable) {
        Page<Course> courses = courseService.searchCourses(keyword, pageable);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<Course>> getCoursesByInstructor(@PathVariable Long instructorId) {
        List<Course> courses = courseService.getCoursesByInstructor(instructorId);
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/{courseId}/modules")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Module> addModuleToCourse(@PathVariable Long courseId, @Valid @RequestBody Module module) {
        Module createdModule = courseService.addModuleToCourse(courseId, module);
        return ResponseEntity.ok(createdModule);
    }

    @PutMapping("/modules/{moduleId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Module> updateModule(@PathVariable Long moduleId, @Valid @RequestBody Module module) {
        Module updatedModule = courseService.updateModule(moduleId, module);
        return ResponseEntity.ok(updatedModule);
    }

    @DeleteMapping("/modules/{moduleId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteModule(@PathVariable Long moduleId) {
        courseService.deleteModule(moduleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{courseId}/modules")
    public ResponseEntity<List<Module>> getCourseModules(@PathVariable Long courseId) {
        List<Module> modules = courseService.getCourseModules(courseId);
        return ResponseEntity.ok(modules);
    }
}
