package sunflower.server.application;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import sunflower.server.application.event.TranslationsSaveEvent;
import sunflower.server.client.OcrDownloadClient;
import sunflower.server.client.OcrRegisterClient;
import sunflower.server.client.OcrStatusClient;
import sunflower.server.entity.Translations;
import sunflower.server.repository.TranslationsRepository;

import java.io.File;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Slf4j
@NoArgsConstructor
@Component
public class TranslationsSaveEventListener {

    private TranslationsRepository translationsRepository;
    private OcrRegisterClient ocrRegisterClient;
    private OcrStatusClient ocrStatusClient;
    private OcrDownloadClient ocrDownloadClient;

    @Autowired
    public TranslationsSaveEventListener(
            final TranslationsRepository translationsRepository,
            final OcrRegisterClient ocrRegisterClient,
            final OcrStatusClient ocrStatusClient,
            final OcrDownloadClient ocrDownloadClient
    ) {
        this.translationsRepository = translationsRepository;
        this.ocrRegisterClient = ocrRegisterClient;
        this.ocrStatusClient = ocrStatusClient;
        this.ocrDownloadClient = ocrDownloadClient;
    }

    @Async
    @TransactionalEventListener
    @Transactional(propagation = REQUIRES_NEW)
    public void handleTranslationsSave(final TranslationsSaveEvent event) {
        log.info("현재 스레드: {}", Thread.currentThread().getName());

        final Translations translations = translationsRepository.findById(event.getTranslations().getId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Translation입니다!"));
        final String pdfURI = translations.getPdfURI().replace("file:", ""); // 확인 필요
        final File file = Paths.get(pdfURI).toFile();

        translations.startOcr();
        final String pdfId = ocrRegisterClient.requestPdfId(file);
        translations.setOcrPdfId(pdfId);
    }
}
