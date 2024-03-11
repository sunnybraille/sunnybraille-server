package sunflower.server.application.eventlistener;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import sunflower.server.application.event.OcrRegisterEvent;
import sunflower.server.application.event.OcrStatusEvent;
import sunflower.server.client.OcrRegisterClient;
import sunflower.server.entity.Transcriptions;
import sunflower.server.repository.TranscriptionsRepository;
import sunflower.server.util.FileUtil;

import java.io.File;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Slf4j
@NoArgsConstructor
@Component
public class OcrRegisterEventListener {

    private TranscriptionsRepository transcriptionsRepository;
    private OcrRegisterClient ocrRegisterClient;
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    public OcrRegisterEventListener(
            final TranscriptionsRepository transcriptionsRepository,
            final OcrRegisterClient ocrRegisterClient,
            final ApplicationEventPublisher eventPublisher
    ) {
        this.transcriptionsRepository = transcriptionsRepository;
        this.ocrRegisterClient = ocrRegisterClient;
        this.eventPublisher = eventPublisher;
    }

    @Async
    @TransactionalEventListener
    @Transactional(propagation = REQUIRES_NEW)
    public void registerOcr(final OcrRegisterEvent event) {
        log.info("현재 스레드: {}", Thread.currentThread().getName());

        final Long id = event.getTranscriptions().getId();
        final Transcriptions transcriptions = transcriptionsRepository.getById(id);
        final File file = FileUtil.findFile(transcriptions.getPdfPath());

        transcriptions.startOcr();
        final String pdfId = ocrRegisterClient.requestPdfId(file);
        transcriptions.registerPdfId(pdfId);

        eventPublisher.publishEvent(new OcrStatusEvent(this, id));
    }
}
