package sunflower.server.util;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileUtil {

    public static String savePdfFile(final MultipartFile file, String fileName) {
        final Path path = Paths.get("src", "main", "pdf", fileName);

        try {
            Files.copy(file.getInputStream(), path);
            Resource resource = new FileSystemResource(path.toFile());
            return resource.getFile().getPath();
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

    public static String saveLatexFile(final String pdfId, byte[] content) {
        final File file = new File("src/main/latex/" + pdfId + ".zip");

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content);
        } catch (IOException e) {
            throw new RuntimeException("파일을 읽는데 실패함!");
        }
        return file.getPath();
    }
}
