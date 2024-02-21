package sunflower.server.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sunflower.server.application.dto.TranslationsStatusDto;
import sunflower.server.application.event.TranslationsSaveEvent;
import sunflower.server.client.OcrDownloadClient;
import sunflower.server.client.OcrProgressClient;
import sunflower.server.client.OcrRequestClient;
import sunflower.server.entity.Translations;
import sunflower.server.repository.TranslationsRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class TranslationsService {

    private final TranslationsRepository translationsRepository;
    private final ResourceLoader resourceLoader;
    private final OcrRequestClient ocrRequestClient;
    private final OcrProgressClient ocrProgressClient;
    private final OcrDownloadClient ocrDownloadClient;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Long register(final MultipartFile file) {
        final String pdfURI = saveFile(file);
        log.info("Saved pdf File in Server. File URI: {}", pdfURI);
        final Translations translations = translationsRepository.save(Translations.of(pdfURI));

        eventPublisher.publishEvent(new TranslationsSaveEvent(this, translations));

        return translations.getId();
    }

    private String saveFile(final MultipartFile file) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        final Path path = Paths.get("src", "main", "pdf", fileName);

        try {
            Files.copy(file.getInputStream(), path);
            final Resource resource = resourceLoader.getResource("file:" + path);
            return resource.getURI().toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public TranslationsStatusDto status(final Long id) {
        final Translations translations = translationsRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(id + "에 해당하는 자료가 존재하지 않습니다."));

        return TranslationsStatusDto.from(translations);
    }

    public Long translate(final MultipartFile file) {
        final String fileName = file.getOriginalFilename();
        log.info("File: {}, Mathpix API 호출을 시작합니다.", fileName);

        final String pdfId = ocrRequestClient.requestPdfId(file);
        log.info("Mathpix API로부터 pdf id를 받았습니다. File: {}, pdf id: {}", fileName, pdfId);

        final boolean isDone = ocrProgressClient.isDone(pdfId);
        log.info("Mathpix API의 OCR 작업이 완료되었습니다. File: {}, pdf id: {}", fileName, pdfId);

        final File latexFile = ocrDownloadClient.download(pdfId);
        log.info("Latex 파일을 다운로드했습니다. File Name: {}", latexFile.getName());

        return null;
    }
}
