package sunflower.server.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Profile("!test")
public class PdfTranslationService {

    private final String appId;
    private final String appKey;

    public PdfTranslationService(
            @Value("${mathpix.app-id}") String appId,
            @Value("${mathpix.app-key}") String appKey
    ) {
        this.appId = appId;
        this.appKey = appKey;
    }

    public Long translate(final MultipartFile file) {
        return null;
    }
}
