package sunflower.server.application.eventlistener;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import sunflower.server.application.event.OcrDownloadEvent;
import sunflower.server.application.event.TranslateEvent;
import sunflower.server.client.OcrDownloadClient;
import sunflower.server.entity.Translations;
import sunflower.server.repository.TranslationsRepository;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;
import static sunflower.server.util.FileUtil.saveLatexFile;

@Slf4j
@NoArgsConstructor
@Component
public class OcrDownloadEventListener {

    private TranslationsRepository translationsRepository;
    private OcrDownloadClient ocrDownloadClient;
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    public OcrDownloadEventListener(
            final TranslationsRepository translationsRepository,
            final OcrDownloadClient ocrDownloadClient,
            final ApplicationEventPublisher eventPublisher
    ) {
        this.translationsRepository = translationsRepository;
        this.ocrDownloadClient = ocrDownloadClient;
        this.eventPublisher = eventPublisher;
    }

    @Async
    @TransactionalEventListener
    @Transactional(propagation = REQUIRES_NEW)
    public void downloadLatexFile(final OcrDownloadEvent event) {
        final Translations translations = translationsRepository.getById(event.getId());
        final String pdfId = translations.getOcrPdfId();

        final byte[] latex = ocrDownloadClient.download(pdfId);
        final String latexPath = saveLatexFile(pdfId, latex);
        translations.registerLatexPath(latexPath);

        eventPublisher.publishEvent(new TranslateEvent(this, event.getId()));
    }
}
