package sunflower.server.client;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface OcrRegisterClient {

    @Deprecated
    String requestPdfId(final MultipartFile file);

    String requestPdfId(final File file);
}

