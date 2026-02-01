package in.maven.ark.lms.content.service;

import in.maven.ark.lms.content.entity.MediaContent;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class VideoProcessingService {
    public void processVideo(MediaContent content, Path storagePath) {
        // Implementation for video processing
    }

    public String generateVideoThumbnail(MediaContent content, Path thumbnailPath) {
        // Implementation for video thumbnail generation
        return thumbnailPath.toString();
    }
}
