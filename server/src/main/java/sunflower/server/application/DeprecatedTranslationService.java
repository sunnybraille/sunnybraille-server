package sunflower.server.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sunflower.server.client.OcrDownloadClient;
import sunflower.server.client.OcrRegisterClient;
import sunflower.server.client.OcrStatusClient;
import sunflower.server.util.FileSaveUtil;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeprecatedTranslationService {

    private final OcrRegisterClient ocrRegisterClient;
    private final OcrStatusClient ocrStatusClient;
    private final OcrDownloadClient ocrDownloadClient;

    @Deprecated
    public Long translate(final MultipartFile file) {
        final String fileName = file.getOriginalFilename();
        log.info("File: {}, Mathpix API 호출을 시작합니다.", fileName);

        final String pdfId = ocrRegisterClient.requestPdfId(file);
        log.info("Mathpix API로부터 pdf id를 받았습니다. File: {}, pdf id: {}", fileName, pdfId);

        final boolean isDone = ocrStatusClient.isDone(pdfId);
        log.info("Mathpix API의 OCR 작업이 완료되었습니다. File: {}, pdf id: {}", fileName, pdfId);

        final byte[] latex = ocrDownloadClient.download(pdfId);
        FileSaveUtil.saveLatexFile(pdfId, latex);

        return null;
    }
}
