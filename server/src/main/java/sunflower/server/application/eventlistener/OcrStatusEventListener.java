package sunflower.server.application.eventlistener;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import sunflower.server.application.event.OcrDownloadEvent;
import sunflower.server.application.event.OcrStatusEvent;
import sunflower.server.client.OcrStatusClient;
import sunflower.server.client.dto.OcrStatus;
import sunflower.server.client.dto.OcrStatusDto;
import sunflower.server.entity.Translations;
import sunflower.server.repository.TranslationsRepository;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Slf4j
@NoArgsConstructor
@Component
public class OcrStatusEventListener {

    private TranslationsRepository translationsRepository;
    private OcrStatusClient ocrStatusClient;
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    public OcrStatusEventListener(
            final TranslationsRepository translationsRepository,
            final OcrStatusClient ocrStatusClient,
            final ApplicationEventPublisher eventPublisher
    ) {
        this.translationsRepository = translationsRepository;
        this.ocrStatusClient = ocrStatusClient;
        this.eventPublisher = eventPublisher;
    }

    @Async
    @TransactionalEventListener
    @Transactional(propagation = REQUIRES_NEW)
    public void checkOcrStatus(final OcrStatusEvent event) {
        log.info("현재 스레드: {}", Thread.currentThread().getName());

        final Long id = event.getId();
        final Translations translations = translationsRepository.getById(id);

        final String pdfId = translations.getOcrPdfId();
        final OcrStatusDto status = ocrStatusClient.checkStatus(pdfId);
        translations.changeOcrStatus(status);

        if (status.getStatus() != OcrStatus.COMPLETED) {
            retryCheckOcrStatus(id, pdfId);
            return;
        }

        eventPublisher.publishEvent(new OcrDownloadEvent(this, id));
    }

    @Scheduled(fixedDelay = 1000)
    private void retryCheckOcrStatus(final Long id, final String pdfId) {
        log.info("Cheking OCR status for translations id: {}, pdf id: {}", id, pdfId);
        eventPublisher.publishEvent(new OcrStatusEvent(this, id));
    }
}
