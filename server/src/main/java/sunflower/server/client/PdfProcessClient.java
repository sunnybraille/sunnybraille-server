package sunflower.server.client;

import org.springframework.web.multipart.MultipartFile;

public interface PdfProcessClient {

    String requestPdfId(final MultipartFile file);
}

