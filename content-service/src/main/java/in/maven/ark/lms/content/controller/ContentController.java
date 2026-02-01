package in.maven.ark.lms.content.controller;

import in.maven.ark.lms.content.dto.ContentUploadRequest;
import in.maven.ark.lms.content.dto.ContentResponse;
import in.maven.ark.lms.content.entity.MediaContent;
import in.maven.ark.lms.content.service.ContentManagementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/content")
public class ContentController {

    @Autowired
    private ContentManagementService contentManagementService;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ContentResponse> uploadContent(
            @RequestParam("file") MultipartFile file,
            @RequestParam("lessonId") Long lessonId,
            @RequestParam("contentType") MediaContent.ContentType contentType,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("uploadedBy") Long uploadedBy) {
        
        ContentUploadRequest request = new ContentUploadRequest();
        request.setLessonId(lessonId);
        request.setContentType(contentType);
        request.setTitle(title);
        request.setDescription(description);
        request.setUploadedBy(uploadedBy);
        
        ContentResponse response = contentManagementService.uploadContent(file, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<ContentResponse> getContent(@PathVariable Long contentId) {
        ContentResponse response = contentManagementService.getContent(contentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<List<ContentResponse>> getContentByLesson(@PathVariable Long lessonId) {
        List<ContentResponse> contents = contentManagementService.getContentByLesson(lessonId);
        return ResponseEntity.ok(contents);
    }

    @PutMapping("/{contentId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ContentResponse> updateContent(
            @PathVariable Long contentId,
            @Valid @RequestBody ContentUploadRequest request) {
        ContentResponse response = contentManagementService.updateContent(contentId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{contentId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteContent(@PathVariable Long contentId) {
        contentManagementService.deleteContent(contentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{contentId}/download")
    public ResponseEntity<Resource> downloadContent(@PathVariable Long contentId) {
        try {
            ContentResponse content = contentManagementService.getContent(contentId);
            
            // Read file from storage
            Path filePath = Paths.get(content.getRelativePath());
            Resource resource = new ByteArrayResource(Files.readAllBytes(filePath));
            
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                            "attachment; filename=\"" + content.getOriginalFileName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{contentId}/stream")
    public ResponseEntity<Resource> streamContent(@PathVariable Long contentId) {
        try {
            ContentResponse content = contentManagementService.getContent(contentId);
            
            // Read file from storage
            Path filePath = Paths.get(content.getRelativePath());
            Resource resource = new ByteArrayResource(Files.readAllBytes(filePath));
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(content.getMimeType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{contentId}/url")
    public ResponseEntity<String> getContentUrl(@PathVariable Long contentId) {
        String url = contentManagementService.getContentUrl(contentId);
        return ResponseEntity.ok(url);
    }

    @PostMapping("/{contentId}/thumbnail")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ContentResponse> generateThumbnail(@PathVariable Long contentId) {
        ContentResponse response = contentManagementService.generateThumbnail(contentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/thumbnail/{thumbnailPath}")
    public ResponseEntity<Resource> getThumbnail(@PathVariable String thumbnailPath) {
        try {
            // Read thumbnail file from storage
            Path filePath = Paths.get("content-storage/thumbnails", thumbnailPath);
            Resource resource = new ByteArrayResource(Files.readAllBytes(filePath));
            
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Content Service is running");
    }
}
