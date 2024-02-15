package sunflower.server.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sunflower.server.client.PdfProcessClient;
import sunflower.server.client.PdfQueryClient;

@Slf4j
@RequiredArgsConstructor
@Service
public class PdfTranslationService {

    private final PdfProcessClient pdfProcessClient;
    private final PdfQueryClient pdfQueryClient;

    public Long translate(final MultipartFile file) {
        log.info("File: {}, Mathpix API 호출을 시작합니다.", file.getOriginalFilename());
        final String pdfId = pdfProcessClient.requestPdfId(file);
        final Object ocr = pdfQueryClient.queryPdfBy(pdfId);
        return null;
    }
}
