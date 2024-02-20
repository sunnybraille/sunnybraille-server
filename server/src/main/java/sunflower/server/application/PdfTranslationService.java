package sunflower.server.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sunflower.server.client.OcrDownloadClient;
import sunflower.server.client.OcrProgressClient;
import sunflower.server.client.OcrRequestClient;

import java.io.File;

@Slf4j
@RequiredArgsConstructor
@Service
public class PdfTranslationService {

    private final OcrRequestClient ocrRequestClient;
    private final OcrProgressClient ocrProgressClient;
    private final OcrDownloadClient ocrDownloadClient;

    public Long translate(final MultipartFile file) {
        final String fileName = file.getOriginalFilename();
        log.info("File: {}, Mathpix API 호출을 시작합니다.", fileName);

        final String pdfId = ocrRequestClient.requestPdfId(file);
        log.info("Mathpix API로부터 pdf id를 받았습니다. File: {}, pdf id: {}", fileName, pdfId);

        final boolean isDone = ocrProgressClient.isDone(pdfId);
        log.info("Mathpix API의 OCR 작업이 완료되었습니다. File: {}, pdf id: {}", fileName, pdfId);

        final File latexFile = ocrDownloadClient.download(pdfId);
        System.out.println("latexFile.getAbsolutePath() = " + latexFile.getAbsolutePath());

        return null;
    }
}
