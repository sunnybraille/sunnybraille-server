package sunflower.server.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sunflower.server.application.dto.BrfFileDto;
import sunflower.server.application.dto.TranscriptionStatusDto;
import sunflower.server.application.event.OcrRegisterEvent;
import sunflower.server.entity.Transcriptions;
import sunflower.server.repository.TranscriptionsRepository;
import sunflower.server.util.FileUtil;

import java.io.File;
import java.nio.file.Paths;

@Slf4j
@RequiredArgsConstructor
@Service
public class TranscriptionService {

    private final TranscriptionsRepository transcriptionsRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Long register(final MultipartFile file) {
        final String originalFileName = file.getOriginalFilename();
        final String fileName = FileUtil.createRandomFileName(file);

        final String pdfPath = FileUtil.saveFile(file, fileName, Paths.get("src", "main", "pdf"));
        final Transcriptions transcriptions = transcriptionsRepository.save(Transcriptions.of(pdfPath, originalFileName));
        log.info("Saved pdf File in Server. File URI: {}", pdfPath);

        eventPublisher.publishEvent(new OcrRegisterEvent(this, transcriptions));
        log.info("pdf file 저장 이벤트를 발행했습니다!");
        log.info("현재 스레드: {}", Thread.currentThread().getName());

        return transcriptions.getId();
    }

    @Transactional
    public TranscriptionStatusDto status(final Long id) {
        return TranscriptionStatusDto.from(transcriptionsRepository.getById(id));
    }

    public BrfFileDto findBrfFileById(final Long id) {
        final Transcriptions transcriptions = transcriptionsRepository.getById(id);
        final File file = FileUtil.findFile(transcriptions.getBrfPath());

        final String content = FileUtil.readFile(file);
        return BrfFileDto.of(transcriptions, content);
    }
}
