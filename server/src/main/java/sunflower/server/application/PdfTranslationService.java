package sunflower.server.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sunflower.server.client.MathpixApiPdfProcessClient;
import sunflower.server.client.MathpixApiPdfQueryClient;

@Slf4j
@Profile("!test")
@RequiredArgsConstructor
@Service
public class PdfTranslationService {

    private final MathpixApiPdfProcessClient pdfProcessClient;
    private final MathpixApiPdfQueryClient pdfQueryClient;

    public Long translate(final MultipartFile file) {
        log.info("File: {}, Mathpix API 호출을 시작합니다.", file.getOriginalFilename());
        final String pdfId = pdfProcessClient.requestPdfId(file);
        pdfQueryClient.queryPdfBy(pdfId);
        return null;
    }
}
