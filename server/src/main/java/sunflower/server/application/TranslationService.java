package sunflower.server.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sunflower.server.application.dto.TranslationStatusDto;
import sunflower.server.application.event.OcrRegisterEvent;
import sunflower.server.entity.Translations;
import sunflower.server.repository.TranslationsRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class TranslationService {

    private final TranslationsRepository translationsRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Long register(final MultipartFile file) {
        final String pdfURI = saveFile(file);
        log.info("Saved pdf File in Server. File URI: {}", pdfURI);

        final Translations translations = translationsRepository.save(Translations.of(pdfURI, file.getOriginalFilename()));

        eventPublisher.publishEvent(new OcrRegisterEvent(this, translations));
        log.info("pdf file 저장 이벤트를 발행했습니다!");
        log.info("현재 스레드: {}", Thread.currentThread().getName());

        return translations.getId();
    }

    private String saveFile(final MultipartFile file) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        final Path path = Paths.get("src", "main", "pdf", fileName);

        try {
            Files.copy(file.getInputStream(), path);
            Resource resource = new FileSystemResource(path.toFile());
            return resource.getURI().getPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public TranslationStatusDto status(final Long id) {
        final Translations translations = translationsRepository.getById(id);

        return TranslationStatusDto.from(translations);
    }
}
