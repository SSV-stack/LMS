package in.maven.ark.lms.content.service;

import in.maven.ark.lms.content.dto.ContentUploadRequest;
import in.maven.ark.lms.content.dto.ContentResponse;
import in.maven.ark.lms.content.entity.MediaContent;
import in.maven.ark.lms.content.repository.MediaContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ContentManagementService {

    @Autowired
    private MediaContentRepository mediaContentRepository;

    @Autowired
    private VideoProcessingService videoProcessingService;

    @Autowired
    private DocumentProcessingService documentProcessingService;

    @Autowired
    private ImageProcessingService imageProcessingService;

    @Value("${content.storage.base-path}")
    private String storageBasePath;

    @Value("${content.cdn-base-url}")
    private String cdnBaseUrl;

    public ContentResponse uploadContent(MultipartFile file, ContentUploadRequest request) {
        try {
            // Validate file
            validateFile(file, request.getContentType());

            // Generate unique filename
            String fileName = generateUniqueFileName(file.getOriginalFilename());
            String relativePath = request.getContentType().name().toLowerCase() + "/" + fileName;
            Path storagePath = Paths.get(storageBasePath, relativePath);

            // Create directories if they don't exist
            Files.createDirectories(storagePath.getParent());

            // Save file to storage
            Files.copy(file.getInputStream(), storagePath);

            // Create media content entity
            MediaContent content = new MediaContent();
            content.setLessonId(request.getLessonId());
            content.setContentType(request.getContentType());
            content.setOriginalFileName(file.getOriginalFilename());
            content.setStoredFileName(fileName);
            content.setRelativePath(relativePath);
            content.setFileSize(file.getSize());
            content.setMimeType(file.getContentType());
            content.setStatus(MediaContent.Status.UPLOADED);
            content.setUploadedBy(request.getUploadedBy());
            content.setUploadedAt(LocalDateTime.now());

            // Process content based on type
            processContent(content, storagePath);

            // Save to database
            content = mediaContentRepository.save(content);

            return convertToResponse(content);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload content", e);
        }
    }

    public ContentResponse getContent(Long contentId) {
        MediaContent content = mediaContentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Content not found"));
        return convertToResponse(content);
    }

    public List<ContentResponse> getContentByLesson(Long lessonId) {
        List<MediaContent> contents = mediaContentRepository.findByLessonIdOrderByUploadedAtDesc(lessonId);
        return contents.stream()
                .map(this::convertToResponse)
                .toList();
    }

    public ContentResponse updateContent(Long contentId, ContentUploadRequest request) {
        MediaContent content = mediaContentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Content not found"));

        content.setTitle(request.getTitle());
        content.setDescription(request.getDescription());
        content.setUpdatedAt(LocalDateTime.now());

        content = mediaContentRepository.save(content);
        return convertToResponse(content);
    }

    public void deleteContent(Long contentId) {
        MediaContent content = mediaContentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Content not found"));

        try {
            // Delete file from storage
            Path storagePath = Paths.get(storageBasePath, content.getRelativePath());
            Files.deleteIfExists(storagePath);

            // Delete from database
            mediaContentRepository.delete(content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete content", e);
        }
    }

    public String getContentUrl(Long contentId) {
        MediaContent content = mediaContentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Content not found"));
        
        return cdnBaseUrl + "/" + content.getRelativePath();
    }

    public ContentResponse generateThumbnail(Long contentId) {
        MediaContent content = mediaContentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Content not found"));

        if (content.getContentType() == MediaContent.ContentType.VIDEO || 
            content.getContentType() == MediaContent.ContentType.IMAGE) {
            
            try {
                String thumbnailPath = generateThumbnailForContent(content);
                content.setThumbnailPath(thumbnailPath);
                content.setUpdatedAt(LocalDateTime.now());
                content = mediaContentRepository.save(content);
                
                return convertToResponse(content);
            } catch (Exception e) {
                throw new RuntimeException("Failed to generate thumbnail", e);
            }
        }
        
        throw new RuntimeException("Thumbnail generation not supported for content type: " + content.getContentType());
    }

    private void validateFile(MultipartFile file, MediaContent.ContentType contentType) {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        // Validate file size based on content type
        long maxSize = getMaxFileSizeForType(contentType);
        if (file.getSize() > maxSize) {
            throw new RuntimeException("File size exceeds maximum allowed size for " + contentType);
        }

        // Validate file format
        String fileExtension = getFileExtension(file.getOriginalFilename());
        if (!isSupportedFormat(fileExtension, contentType)) {
            throw new RuntimeException("File format not supported for " + contentType);
        }
    }

    private void processContent(MediaContent content, Path storagePath) {
        switch (content.getContentType()) {
            case VIDEO:
                videoProcessingService.processVideo(content, storagePath);
                break;
            case DOCUMENT:
                documentProcessingService.processDocument(content, storagePath);
                break;
            case IMAGE:
                imageProcessingService.processImage(content, storagePath);
                break;
            case AUDIO:
                // Process audio if needed
                break;
        }
    }

    private String generateThumbnailForContent(MediaContent content) throws IOException {
        String thumbnailFileName = "thumb_" + content.getStoredFileName();
        String thumbnailRelativePath = "thumbnails/" + thumbnailFileName;
        Path thumbnailPath = Paths.get(storageBasePath, thumbnailRelativePath);
        
        // Create directories
        Files.createDirectories(thumbnailPath.getParent());
        
        // Generate thumbnail based on content type
        if (content.getContentType() == MediaContent.ContentType.VIDEO) {
            return videoProcessingService.generateVideoThumbnail(content, thumbnailPath);
        } else if (content.getContentType() == MediaContent.ContentType.IMAGE) {
            return imageProcessingService.generateImageThumbnail(content, thumbnailPath);
        }
        
        return thumbnailRelativePath;
    }

    private String generateUniqueFileName(String originalFileName) {
        String extension = getFileExtension(originalFileName);
        return UUID.randomUUID().toString() + "." + extension;
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    private long getMaxFileSizeForType(MediaContent.ContentType contentType) {
        return switch (contentType) {
            case VIDEO -> 500 * 1024 * 1024L; // 500MB
            case DOCUMENT -> 50 * 1024 * 1024L; // 50MB
            case IMAGE -> 10 * 1024 * 1024L; // 10MB
            case AUDIO -> 100 * 1024 * 1024L; // 100MB
        };
    }

    private boolean isSupportedFormat(String extension, MediaContent.ContentType contentType) {
        return switch (contentType) {
            case VIDEO -> List.of("mp4", "avi", "mov", "wmv", "flv", "webm").contains(extension);
            case DOCUMENT -> List.of("pdf", "epub", "doc", "docx", "ppt", "pptx", "txt").contains(extension);
            case IMAGE -> List.of("jpg", "jpeg", "png", "gif", "svg", "webp").contains(extension);
            case AUDIO -> List.of("mp3", "wav", "flac", "aac", "ogg").contains(extension);
        };
    }

    private ContentResponse convertToResponse(MediaContent content) {
        ContentResponse response = new ContentResponse();
        response.setId(content.getId());
        response.setLessonId(content.getLessonId());
        response.setContentType(content.getContentType());
        response.setTitle(content.getTitle());
        response.setDescription(content.getDescription());
        response.setOriginalFileName(content.getOriginalFileName());
        response.setFileSize(content.getFileSize());
        response.setMimeType(content.getMimeType());
        content.setUrl(cdnBaseUrl + "/" + content.getRelativePath());
        response.setUrl(content.getUrl());
        response.setThumbnailUrl(content.getThumbnailPath() != null ? 
            cdnBaseUrl + "/" + content.getThumbnailPath() : null);
        response.setStatus(content.getStatus());
        response.setUploadedBy(content.getUploadedBy());
        response.setUploadedAt(content.getUploadedAt());
        response.setUpdatedAt(content.getUpdatedAt());
        response.setRelativePath(content.getRelativePath());
        return response;
    }
}
