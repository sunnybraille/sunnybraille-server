package sunflower.server.client;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import sunflower.server.client.dto.OcrProgressDto;

@Component
@Profile("test")
public class MockOcrProgressClient implements OcrProgressClient {

    @Override
    public OcrProgressDto progress(final String pdfId) {
        return null;
    }

    @Override
    public boolean isDone(final String pdfId) {
        return true;
    }
}
