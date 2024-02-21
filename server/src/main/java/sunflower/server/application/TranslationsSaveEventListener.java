package sunflower.server.application;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import sunflower.server.application.event.TranslationsSaveEvent;
import sunflower.server.client.OcrDownloadClient;
import sunflower.server.client.OcrProgressClient;
import sunflower.server.client.OcrRequestClient;
import sunflower.server.entity.Translations;
import sunflower.server.repository.TranslationsRepository;

import java.io.File;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

@Slf4j
@NoArgsConstructor
@Component
public class TranslationsSaveEventListener {

    private TranslationsRepository translationsRepository;
    private OcrRequestClient ocrRequestClient;
    private OcrProgressClient ocrProgressClient;
    private OcrDownloadClient ocrDownloadClient;

    @Autowired
    public TranslationsSaveEventListener(
            final TranslationsRepository translationsRepository,
            final OcrRequestClient ocrRequestClient,
            final OcrProgressClient ocrProgressClient,
            final OcrDownloadClient ocrDownloadClient
    ) {
        this.translationsRepository = translationsRepository;
        this.ocrRequestClient = ocrRequestClient;
        this.ocrProgressClient = ocrProgressClient;
        this.ocrDownloadClient = ocrDownloadClient;
    }

    @Async
    @TransactionalEventListener
    public void handleTranslationsSave(final TranslationsSaveEvent event) {
        final Translations translations = translationsRepository.findById(event.getTranslations().getId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Translation입니다!"));
        final String pdfURI = translations.getPdfURI().replace("file:", ""); // 일단 어쩔 수 없이 했는데 코드 파악 필요
        final File file = Paths.get(pdfURI).toFile();

        translations.startOcr();
        final String pdfId = ocrRequestClient.requestPdfId(file);
        translations.setOcrPdfId(pdfId);
        translationsRepository.save(translations);
    }
}
