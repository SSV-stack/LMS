package in.maven.ark.lms.content.dto;

import in.maven.ark.lms.content.entity.MediaContent;
import java.time.LocalDateTime;

public class ContentResponse {
    
    private Long id;
    private Long lessonId;
    private MediaContent.ContentType contentType;
    private String title;
    private String description;
    private String originalFileName;
    private Long fileSize;
    private String mimeType;
    private String url;
    private String thumbnailUrl;
    private MediaContent.Status status;
    private Long uploadedBy;
    private LocalDateTime uploadedAt;
    private LocalDateTime updatedAt;
    private String relativePath;

    public ContentResponse() {}

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getLessonId() { return lessonId; }
    public void setLessonId(Long lessonId) { this.lessonId = lessonId; }

    public MediaContent.ContentType getContentType() { return contentType; }
    public void setContentType(MediaContent.ContentType contentType) { this.contentType = contentType; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getOriginalFileName() { return originalFileName; }
    public void setOriginalFileName(String originalFileName) { this.originalFileName = originalFileName; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public MediaContent.Status getStatus() { return status; }
    public void setStatus(MediaContent.Status status) { this.status = status; }

    public Long getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(Long uploadedBy) { this.uploadedBy = uploadedBy; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getRelativePath() { return relativePath; }
    public void setRelativePath(String relativePath) { this.relativePath = relativePath; }
}
