package sunflower.server.util;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileSaveUtil {

    public static String savePdfFile(final MultipartFile file, String fileName) {
        final Path path = Paths.get("src", "main", "pdf", fileName);

        try {
            Files.copy(file.getInputStream(), path);
            Resource resource = new FileSystemResource(path.toFile());
            return resource.getURI().getPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static FileSystemResource convertToFileSystemResource(final MultipartFile file) {
        return (FileSystemResource) file.getResource();
    }

    public static FileSystemResource convertToFileSystemResource(final File file) {
        return new FileSystemResource(file);
    }
}
