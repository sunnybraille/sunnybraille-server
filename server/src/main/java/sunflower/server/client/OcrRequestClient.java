package sunflower.server.client;

import org.springframework.web.multipart.MultipartFile;

public interface OcrRequestClient {

    String requestPdfId(final MultipartFile file);
}

