package sunflower.server.application.eventlistener;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import sunflower.server.application.event.TranscribeEvent;
import sunflower.server.application.event.OcrDownloadEvent;
import sunflower.server.client.OcrDownloadClient;
import sunflower.server.entity.Transcriptions;
import sunflower.server.repository.TranscriptionsRepository;
import sunflower.server.util.FileUtil;

import java.nio.file.Paths;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Slf4j
@NoArgsConstructor
@Component
public class OcrDownloadEventListener {

    private TranscriptionsRepository transcriptionsRepository;
    private OcrDownloadClient ocrDownloadClient;
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    public OcrDownloadEventListener(
            final TranscriptionsRepository transcriptionsRepository,
            final OcrDownloadClient ocrDownloadClient,
            final ApplicationEventPublisher eventPublisher
    ) {
        this.transcriptionsRepository = transcriptionsRepository;
        this.ocrDownloadClient = ocrDownloadClient;
        this.eventPublisher = eventPublisher;
    }

    @Async
    @TransactionalEventListener
    @Transactional(propagation = REQUIRES_NEW)
    public void downloadLatexFile(final OcrDownloadEvent event) {
        final Transcriptions transcriptions = transcriptionsRepository.getById(event.getId());
        final String pdfId = transcriptions.getOcrPdfId();

        final byte[] latex = ocrDownloadClient.download(pdfId);
        final String latexPath = FileUtil.saveFile(latex, pdfId, Paths.get("src", "main", "latex"));
        log.info("Latex File 저장! 경로: {}", latexPath);
        transcriptions.registerLatexPath(latexPath);

        eventPublisher.publishEvent(new TranscribeEvent(this, event.getId()));
    }
}
