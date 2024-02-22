package sunflower.server.client;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Component
public class MockOcrRegisterClient implements OcrRegisterClient {

    @Override
    public String requestPdfId(final MultipartFile file) {
        return UUID.randomUUID().toString();
    }

    @Override
    public String requestPdfId(final File file) {
        return UUID.randomUUID().toString();
    }
}
