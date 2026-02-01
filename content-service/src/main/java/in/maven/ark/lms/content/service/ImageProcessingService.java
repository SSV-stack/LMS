package in.maven.ark.lms.content.service;

import in.maven.ark.lms.content.entity.MediaContent;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class ImageProcessingService {
    public void processImage(MediaContent content, Path storagePath) {
        // Implementation for image processing
    }

    public String generateImageThumbnail(MediaContent content, Path thumbnailPath) {
        // Implementation for image thumbnail generation
        return thumbnailPath.toString();
    }
}
