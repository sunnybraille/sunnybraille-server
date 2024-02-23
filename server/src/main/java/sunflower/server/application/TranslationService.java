package sunflower.server.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sunflower.server.application.dto.TranslationStatusDto;
import sunflower.server.application.event.OcrRegisterEvent;
import sunflower.server.entity.Translations;
import sunflower.server.repository.TranslationsRepository;
import sunflower.server.util.FileSaveUtil;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class TranslationService {

    private final TranslationsRepository translationsRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Long register(final MultipartFile file) {
        final String originalFileName = file.getOriginalFilename();
        final String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        final String pdfPath = FileSaveUtil.savePdfFile(file, fileName);
        final Translations translations = translationsRepository.save(Translations.of(pdfPath, originalFileName));
        log.info("Saved pdf File in Server. File URI: {}", pdfPath);

        eventPublisher.publishEvent(new OcrRegisterEvent(this, translations));
        log.info("pdf file 저장 이벤트를 발행했습니다!");
        log.info("현재 스레드: {}", Thread.currentThread().getName());

        return translations.getId();
    }

    @Transactional
    public TranslationStatusDto status(final Long id) {
        return TranslationStatusDto.from(translationsRepository.getById(id));
    }
}
