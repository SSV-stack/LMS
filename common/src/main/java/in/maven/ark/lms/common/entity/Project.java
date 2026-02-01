package in.maven.ark.lms.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "projects")
public class Project {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
    
    @NotBlank
    @Column(name = "template_repo_url")
    private String templateRepoUrl;
    
    @Column(name = "problem_statement", columnDefinition = "TEXT")
    private String problemStatement;
    
    @Column(name = "requirements", columnDefinition = "TEXT")
    private String requirements;
    
    @Column(name = "evaluation_criteria", columnDefinition = "TEXT")
    private String evaluationCriteria;
    
    @Column(name = "test_config", columnDefinition = "TEXT")
    private String testConfig;
    
    @Column(name = "starter_branch")
    private String starterBranch = "main";
    
    @Column(name = "solution_branch")
    private String solutionBranch = "solution";
    
    @Column(name = "max_attempts")
    private Integer maxAttempts;
    
    @Column(name = "time_limit_hours")
    private Integer timeLimitHours;
    
    @Column(name = "is_auto_graded")
    private Boolean isAutoGraded = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Lesson getLesson() { return lesson; }
    public void setLesson(Lesson lesson) { this.lesson = lesson; }
    
    public String getTemplateRepoUrl() { return templateRepoUrl; }
    public void setTemplateRepoUrl(String templateRepoUrl) { this.templateRepoUrl = templateRepoUrl; }
    
    public String getProblemStatement() { return problemStatement; }
    public void setProblemStatement(String problemStatement) { this.problemStatement = problemStatement; }
    
    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }
    
    public String getEvaluationCriteria() { return evaluationCriteria; }
    public void setEvaluationCriteria(String evaluationCriteria) { this.evaluationCriteria = evaluationCriteria; }
    
    public String getTestConfig() { return testConfig; }
    public void setTestConfig(String testConfig) { this.testConfig = testConfig; }
    
    public String getStarterBranch() { return starterBranch; }
    public void setStarterBranch(String starterBranch) { this.starterBranch = starterBranch; }
    
    public String getSolutionBranch() { return solutionBranch; }
    public void setSolutionBranch(String solutionBranch) { this.solutionBranch = solutionBranch; }
    
    public Integer getMaxAttempts() { return maxAttempts; }
    public void setMaxAttempts(Integer maxAttempts) { this.maxAttempts = maxAttempts; }
    
    public Integer getTimeLimitHours() { return timeLimitHours; }
    public void setTimeLimitHours(Integer timeLimitHours) { this.timeLimitHours = timeLimitHours; }
    
    public Boolean getIsAutoGraded() { return isAutoGraded; }
    public void setIsAutoGraded(Boolean isAutoGraded) { this.isAutoGraded = isAutoGraded; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
