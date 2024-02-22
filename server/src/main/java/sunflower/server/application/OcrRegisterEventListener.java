package sunflower.server.application;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import sunflower.server.application.event.OcrStatusEvent;
import sunflower.server.client.OcrRegisterClient;
import sunflower.server.entity.Translations;
import sunflower.server.repository.TranslationsRepository;

import java.io.File;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Slf4j
@NoArgsConstructor
@Component
public class OcrRegisterEventListener {

    private TranslationsRepository translationsRepository;
    private OcrRegisterClient ocrRegisterClient;
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    public OcrRegisterEventListener(
            final TranslationsRepository translationsRepository,
            final OcrRegisterClient ocrRegisterClient,
            final ApplicationEventPublisher eventPublisher
    ) {
        this.translationsRepository = translationsRepository;
        this.ocrRegisterClient = ocrRegisterClient;
        this.eventPublisher = eventPublisher;
    }

    @Async
    @TransactionalEventListener
    @Transactional(propagation = REQUIRES_NEW)
    public void registerOcr(final sunflower.server.application.event.OcrRegisterEvent event) {
        log.info("현재 스레드: {}", Thread.currentThread().getName());

        final Translations translations = translationsRepository.findById(event.getTranslations().getId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Translation입니다!"));
        final String pdfURI = translations.getPdfURI().replace("file:", ""); // 확인 필요
        final File file = Paths.get(pdfURI).toFile();

        translations.startOcr();
        final String pdfId = ocrRegisterClient.requestPdfId(file);
        translations.setOcrPdfId(pdfId);

        eventPublisher.publishEvent(new OcrStatusEvent(this, pdfId));
    }
}
