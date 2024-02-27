package sunflower.server.application.eventlistener;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import sunflower.server.application.event.BrailleTranslateEvent;
import sunflower.server.client.ApiBrailleTranslationClient;
import sunflower.server.entity.Translations;
import sunflower.server.repository.TranslationsRepository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Slf4j
@NoArgsConstructor
@Component
public class BrailleTranslateEventListener {

    private TranslationsRepository translationsRepository;
    private ApiBrailleTranslationClient apiBrailleTranslationClient;
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    public BrailleTranslateEventListener(
            final TranslationsRepository translationsRepository,
            final ApiBrailleTranslationClient apiBrailleTranslationClient,
            final ApplicationEventPublisher eventPublisher
    ) {
        this.translationsRepository = translationsRepository;
        this.apiBrailleTranslationClient = apiBrailleTranslationClient;
        this.eventPublisher = eventPublisher;
    }

    @Async
    @TransactionalEventListener
    @Transactional(propagation = REQUIRES_NEW)
    public void downloadLatexFile(final BrailleTranslateEvent event) {
        final Translations translations = translationsRepository.getById(event.getId());

        final String latexPath = translations.getLatexPath();
        final File latexFile = Paths.get(latexPath).toFile();

        if (!latexFile.exists()) {
            throw new RuntimeException("파일이 존재하지 않습니다!");
        }

        final String brfContent = apiBrailleTranslationClient.translate(latexFile);
        final String brfPath = saveBrfFile(brfContent, translations.getOcrPdfId());

        translations.registerBrfPath(brfPath);
    }

    private String saveBrfFile(final String content, final String ocrPdfId) {
        final String directory = "src/main/brf";

        final String fileName = ocrPdfId + ".brf";
        final Path brfPath = Paths.get(directory, fileName);

        final File file = brfPath.toFile();
        try {
            final FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return brfPath.toString();
    }
}
