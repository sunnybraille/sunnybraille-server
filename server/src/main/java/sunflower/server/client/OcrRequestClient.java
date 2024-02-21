package sunflower.server.client;

import org.hibernate.annotations.processing.Find;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface OcrRequestClient {

    String requestPdfId(final MultipartFile file);

    String requestPdfId(final File file);
}

