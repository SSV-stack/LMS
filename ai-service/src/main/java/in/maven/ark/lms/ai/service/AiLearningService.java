package in.maven.ark.lms.ai.service;

import in.maven.ark.lms.ai.dto.*;
import in.maven.ark.lms.ai.entity.AiInteraction;
import in.maven.ark.lms.ai.repository.AiInteractionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AiLearningService {

    @Autowired
    private AiInteractionRepository aiInteractionRepository;

    @Autowired
    private CodeAnalysisService codeAnalysisService;

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private ContentGenerationService contentGenerationService;

    @Value("${ai.recommendations.cache-ttl:3600}")
    private int recommendationCacheTtl;

    public AiTutorResponse getTutorAssistance(AiTutorRequest request) {
        // Log the interaction
        AiInteraction interaction = new AiInteraction();
        interaction.setUserId(request.getUserId());
        interaction.setCourseId(request.getCourseId());
        interaction.setInteractionType(AiInteraction.InteractionType.TUTOR_ASSISTANCE);
        interaction.setRequestData(request.toString());
        interaction.setCreatedAt(LocalDateTime.now());

        try {
            // Process the tutor request
            AiTutorResponse response = processTutorRequest(request);
            
            interaction.setResponseData(response.toString());
            interaction.setStatus(AiInteraction.Status.SUCCESS);
            
            return response;
        } catch (Exception e) {
            interaction.setStatus(AiInteraction.Status.ERROR);
            interaction.setErrorMessage(e.getMessage());
            throw new RuntimeException("Failed to process tutor request", e);
        } finally {
            aiInteractionRepository.save(interaction);
        }
    }

    public CodeAnalysisResponse analyzeCode(CodeAnalysisRequest request) {
        // Log the interaction
        AiInteraction interaction = new AiInteraction();
        interaction.setUserId(request.getUserId());
        interaction.setProjectId(request.getProjectId());
        interaction.setInteractionType(AiInteraction.InteractionType.CODE_ANALYSIS);
        interaction.setRequestData(request.toString());
        interaction.setCreatedAt(LocalDateTime.now());

        try {
            // Analyze the code
            CodeAnalysisResponse response = codeAnalysisService.analyzeCode(request);
            
            interaction.setResponseData(response.toString());
            interaction.setStatus(AiInteraction.Status.SUCCESS);
            
            return response;
        } catch (Exception e) {
            interaction.setStatus(AiInteraction.Status.ERROR);
            interaction.setErrorMessage(e.getMessage());
            throw new RuntimeException("Failed to analyze code", e);
        } finally {
            aiInteractionRepository.save(interaction);
        }
    }

    public List<CourseRecommendation> getCourseRecommendations(Long userId) {
        // Check cache first
        List<CourseRecommendation> cached = recommendationService.getCachedRecommendations(userId);
        if (cached != null) {
            return cached;
        }

        // Log the interaction
        AiInteraction interaction = new AiInteraction();
        interaction.setUserId(userId);
        interaction.setInteractionType(AiInteraction.InteractionType.RECOMMENDATION);
        interaction.setRequestData("Get course recommendations for user: " + userId);
        interaction.setCreatedAt(LocalDateTime.now());

        try {
            // Generate recommendations
            List<CourseRecommendation> recommendations = recommendationService.generateRecommendations(userId);
            
            // Cache the results
            recommendationService.cacheRecommendations(userId, recommendations, recommendationCacheTtl);
            
            interaction.setResponseData(recommendations.toString());
            interaction.setStatus(AiInteraction.Status.SUCCESS);
            
            return recommendations;
        } catch (Exception e) {
            interaction.setStatus(AiInteraction.Status.ERROR);
            interaction.setErrorMessage(e.getMessage());
            throw new RuntimeException("Failed to generate recommendations", e);
        } finally {
            aiInteractionRepository.save(interaction);
        }
    }

    public ContentGenerationResponse generateContent(ContentGenerationRequest request) {
        // Log the interaction
        AiInteraction interaction = new AiInteraction();
        interaction.setUserId(request.getUserId());
        interaction.setCourseId(request.getCourseId());
        interaction.setInteractionType(AiInteraction.InteractionType.CONTENT_GENERATION);
        interaction.setRequestData(request.toString());
        interaction.setCreatedAt(LocalDateTime.now());

        try {
            // Generate content
            ContentGenerationResponse response = contentGenerationService.generateContent(request);
            
            interaction.setResponseData(response.toString());
            interaction.setStatus(AiInteraction.Status.SUCCESS);
            
            return response;
        } catch (Exception e) {
            interaction.setStatus(AiInteraction.Status.ERROR);
            interaction.setErrorMessage(e.getMessage());
            throw new RuntimeException("Failed to generate content", e);
        } finally {
            aiInteractionRepository.save(interaction);
        }
    }

    public ProgressAnalysisResponse analyzeProgress(Long userId, Long courseId) {
        // Log the interaction
        AiInteraction interaction = new AiInteraction();
        interaction.setUserId(userId);
        interaction.setCourseId(courseId);
        interaction.setInteractionType(AiInteraction.InteractionType.PROGRESS_ANALYSIS);
        interaction.setRequestData("Analyze progress for user: " + userId + ", course: " + courseId);
        interaction.setCreatedAt(LocalDateTime.now());

        try {
            // Analyze progress
            ProgressAnalysisResponse response = analyzeUserProgress(userId, courseId);
            
            interaction.setResponseData(response.toString());
            interaction.setStatus(AiInteraction.Status.SUCCESS);
            
            return response;
        } catch (Exception e) {
            interaction.setStatus(AiInteraction.Status.ERROR);
            interaction.setErrorMessage(e.getMessage());
            throw new RuntimeException("Failed to analyze progress", e);
        } finally {
            aiInteractionRepository.save(interaction);
        }
    }

    public List<AiInteraction> getUserInteractions(Long userId) {
        return aiInteractionRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Map<String, Object> getAiAnalytics() {
        Map<String, Object> analytics = Map.of(
            "totalInteractions", aiInteractionRepository.count(),
            "successfulInteractions", aiInteractionRepository.countByStatus(AiInteraction.Status.SUCCESS),
            "errorInteractions", aiInteractionRepository.countByStatus(AiInteraction.Status.ERROR),
            "tutorRequests", aiInteractionRepository.countByInteractionType(AiInteraction.InteractionType.TUTOR_ASSISTANCE),
            "codeAnalyses", aiInteractionRepository.countByInteractionType(AiInteraction.InteractionType.CODE_ANALYSIS),
            "recommendations", aiInteractionRepository.countByInteractionType(AiInteraction.InteractionType.RECOMMENDATION)
        );
        
        return analytics;
    }

    private AiTutorResponse processTutorRequest(AiTutorRequest request) {
        // Mock implementation - would integrate with actual AI service
        AiTutorResponse response = new AiTutorResponse();
        response.setAnswer("This is a mock AI tutor response. In a real implementation, this would call OpenAI/Anthropic API.");
        response.setConfidence(0.85);
        response.setSuggestions(List.of("Study the previous lesson", "Try the practice exercises", "Review the documentation"));
        response.setRelatedTopics(List.of("Java Basics", "Object-Oriented Programming", "Data Structures"));
        return response;
    }

    private ProgressAnalysisResponse analyzeUserProgress(Long userId, Long courseId) {
        // Mock implementation - would analyze actual user data
        ProgressAnalysisResponse response = new ProgressAnalysisResponse();
        response.setOverallProgress(75.0);
        response.setStrengths(List.of("Problem Solving", "Code Quality"));
        response.setWeaknesses(List.of("Testing", "Documentation"));
        response.setRecommendations(List.of("Focus on unit testing", "Add more comments to code", "Practice debugging techniques"));
        response.setEstimatedCompletionDate(LocalDateTime.now().plusWeeks(2));
        return response;
    }
}
