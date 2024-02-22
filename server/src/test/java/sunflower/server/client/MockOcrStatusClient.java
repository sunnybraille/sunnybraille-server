package sunflower.server.client;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import sunflower.server.client.dto.OcrStatusDto;

@Component
@Profile("test")
public class MockOcrStatusClient implements OcrStatusClient {

    @Override
    public OcrStatusDto checkStatus(final String pdfId) {
        return null;
    }

    @Override
    public boolean isDone(final String pdfId) {
        return true;
    }
}
