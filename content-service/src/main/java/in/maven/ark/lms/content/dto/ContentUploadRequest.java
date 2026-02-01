package in.maven.ark.lms.content.dto;

import in.maven.ark.lms.content.entity.MediaContent;
import jakarta.validation.constraints.NotNull;

public class ContentUploadRequest {
    
    @NotNull(message = "Lesson ID is required")
    private Long lessonId;
    
    @NotNull(message = "Content type is required")
    private MediaContent.ContentType contentType;
    
    private String title;
    
    private String description;
    
    @NotNull(message = "Uploaded by is required")
    private Long uploadedBy;

    public ContentUploadRequest() {}

    // Getters and setters
    public Long getLessonId() { return lessonId; }
    public void setLessonId(Long lessonId) { this.lessonId = lessonId; }

    public MediaContent.ContentType getContentType() { return contentType; }
    public void setContentType(MediaContent.ContentType contentType) { this.contentType = contentType; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(Long uploadedBy) { this.uploadedBy = uploadedBy; }
}
