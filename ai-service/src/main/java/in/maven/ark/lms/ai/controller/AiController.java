package in.maven.ark.lms.ai.controller;

import in.maven.ark.lms.ai.dto.*;
import in.maven.ark.lms.ai.entity.AiInteraction;
import in.maven.ark.lms.ai.service.AiLearningService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Autowired
    private AiLearningService aiLearningService;

    @PostMapping("/tutor")
    @PreAuthorize("hasRole('LEARNER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<AiTutorResponse> getTutorAssistance(@Valid @RequestBody AiTutorRequest request) {
        AiTutorResponse response = aiLearningService.getTutorAssistance(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/code-analysis")
    @PreAuthorize("hasRole('LEARNER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<CodeAnalysisResponse> analyzeCode(@Valid @RequestBody CodeAnalysisRequest request) {
        CodeAnalysisResponse response = aiLearningService.analyzeCode(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recommendations/{userId}")
    @PreAuthorize("hasRole('LEARNER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<CourseRecommendation>> getCourseRecommendations(@PathVariable Long userId) {
        List<CourseRecommendation> recommendations = aiLearningService.getCourseRecommendations(userId);
        return ResponseEntity.ok(recommendations);
    }

    @PostMapping("/content-generation")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ContentGenerationResponse> generateContent(@Valid @RequestBody ContentGenerationRequest request) {
        ContentGenerationResponse response = aiLearningService.generateContent(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/progress/{userId}/course/{courseId}")
    @PreAuthorize("hasRole('LEARNER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ProgressAnalysisResponse> analyzeProgress(
            @PathVariable Long userId, 
            @PathVariable Long courseId) {
        ProgressAnalysisResponse response = aiLearningService.analyzeProgress(userId, courseId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/interactions/{userId}")
    @PreAuthorize("hasRole('LEARNER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<AiInteraction>> getUserInteractions(@PathVariable Long userId) {
        List<AiInteraction> interactions = aiLearningService.getUserInteractions(userId);
        return ResponseEntity.ok(interactions);
    }

    @GetMapping("/analytics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getAiAnalytics() {
        Map<String, Object> analytics = aiLearningService.getAiAnalytics();
        return ResponseEntity.ok(analytics);
    }
}
