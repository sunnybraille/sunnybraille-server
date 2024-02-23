package sunflower.server.client;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Profile("test")
public class MockOcrDownloadClient implements OcrDownloadClient {

    @Override
    public byte[] download(final String pdfId) {
        return new File("");
    }
}
