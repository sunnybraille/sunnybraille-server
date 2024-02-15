package sunflower.server.client;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class MockPdfQueryClient implements PdfQueryClient {

    @Override
    public Object queryPdfBy(final String pdfId) {
        return null;
    }
}
