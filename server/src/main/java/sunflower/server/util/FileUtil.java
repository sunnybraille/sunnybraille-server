package sunflower.server.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
public final class FileUtil {

    public static String savePdfFile(final MultipartFile file, String fileName) {
        final Path directoryPath = Paths.get("src", "main", "pdf");
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                throw new RuntimeException("Unable to create directory: " + directoryPath, e);
            }
        }

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
        final Path directoryPath = Paths.get("src", "main", "latex");
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                throw new RuntimeException("Unable to create directory: " + directoryPath, e);
            }
        }

        String path = "src/main/latex/" + pdfId + ".tex";

        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(content))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    Files.copy(zipInputStream, Paths.get(path));
                    break;
                }
            }
        } catch (IOException e) {
            log.error("Error extracting file from zip: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        return path;
    }

    public static String saveBrfFile(final String content, final String ocrPdfId) {
        final Path directoryPath = Paths.get("src", "main", "brf");
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                throw new RuntimeException("Unable to create directory: " + directoryPath, e);
            }
        }

        final String directory = "src/main/brf";
        final String fileName = ocrPdfId + ".brf";
        final Path brfPath = Paths.get(directory, fileName);

        final File file = brfPath.toFile();
        try {
            final FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return brfPath.toString();
    }
}
