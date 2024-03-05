package sunflower.server.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import sunflower.server.exception.ErrorCode;
import sunflower.server.exception.FileException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
public final class FileUtil {

    public static String saveFile(Object file, String fileName, Path directoryPath) {
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                throw new FileException(e, "Unable to create directory: " + directoryPath);
            }
        }

        Path path;
        if (file instanceof MultipartFile) {
            path = saveMultipartFile((MultipartFile) file, fileName, directoryPath);
        } else if (file instanceof String) {
            path = saveStringContent((String) file, fileName, directoryPath);
        } else if (file instanceof byte[]) {
            path = saveZipFile((byte[]) file, fileName, directoryPath);
        } else {
            throw new FileException("Unsupported file type");
        }

        return path.toString();
    }

    private static Path saveMultipartFile(MultipartFile file, String fileName, Path directoryPath) {
        try {
            Path filePath = Paths.get(directoryPath.toString(), fileName);
            Files.copy(file.getInputStream(), filePath);
            return filePath;
        } catch (IOException e) {
            throw new FileException(ErrorCode.P, e);
        }
    }

    private static Path saveStringContent(String content, String fileName, Path directoryPath) {
        Path filePath = Paths.get(directoryPath.toString(), fileName);
        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            writer.write(content);
            return filePath;
        } catch (IOException e) {
            throw new FileException(ErrorCode.B, e);
        }
    }

    private static Path saveZipFile(byte[] content, String fileName, Path directoryPath) {
        try {
            String path = "src/main/latex/" + fileName + ".tex";
            try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(content))) {
                ZipEntry entry;
                while ((entry = zipInputStream.getNextEntry()) != null) {
                    if (!entry.isDirectory()) {
                        Files.copy(zipInputStream, Paths.get(path));
                        break;
                    }
                }
            }
            return Paths.get(path);
        } catch (IOException e) {
            throw new FileException(ErrorCode.L, e);
        }
    }

    public static String createRandomFileName(final MultipartFile file) {
        return UUID.randomUUID() + "_" + file.getOriginalFilename();
    }

    public static File findFile(String path) {
        return Paths.get(path).toFile();
    }

    public static String readFile(final File file) {
        try {
            return new String(Files.readAllBytes(Paths.get(file.getPath())));
        } catch (IOException e) {
            throw new FileException(e);
        }
    }
}
