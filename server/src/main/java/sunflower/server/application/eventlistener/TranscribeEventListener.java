package sunflower.server.application.eventlistener;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import sunflower.server.application.event.TranscribeEvent;
import sunflower.server.client.TranscriptionClient;
import sunflower.server.entity.Transcriptions;
import sunflower.server.exception.FileException;
import sunflower.server.repository.TranscriptionsRepository;
import sunflower.server.util.FileUtil;

import java.io.File;
import java.nio.file.Paths;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Slf4j
@NoArgsConstructor
@Component
public class TranscribeEventListener {

    private TranscriptionsRepository transcriptionsRepository;
    private TranscriptionClient transcriptionClient;

    @Autowired
    public TranscribeEventListener(
            final TranscriptionsRepository transcriptionsRepository,
            final TranscriptionClient transcriptionClient
    ) {
        this.transcriptionsRepository = transcriptionsRepository;
        this.transcriptionClient = transcriptionClient;
    }

    @Async
    @TransactionalEventListener
    @Transactional(propagation = REQUIRES_NEW)
    public void downloadLatexFile(final TranscribeEvent event) {
        final Transcriptions transcriptions = transcriptionsRepository.getById(event.getId());

        final File latexFile = FileUtil.findFile(transcriptions.getLatexPath());

        if (!latexFile.exists()) {
            throw new FileException("파일이 존재하지 않습니다!");
        }

        final String brfContent = transcriptionClient.transcribe(latexFile);
        final String brfPath = FileUtil.saveFile(brfContent, transcriptions.getOcrPdfId(), Paths.get("src", "main", "brf"));

        transcriptions.finishTransbraille(brfPath);
    }
}
