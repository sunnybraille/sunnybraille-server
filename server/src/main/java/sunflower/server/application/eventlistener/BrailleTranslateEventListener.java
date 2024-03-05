package sunflower.server.application.eventlistener;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import sunflower.server.application.event.BrailleTranslateEvent;
import sunflower.server.client.BrailleTranslationClient;
import sunflower.server.entity.Translations;
import sunflower.server.exception.FileException;
import sunflower.server.repository.TranslationsRepository;
import sunflower.server.util.FileUtil;

import java.io.File;
import java.nio.file.Paths;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Slf4j
@NoArgsConstructor
@Component
public class BrailleTranslateEventListener {

    private TranslationsRepository translationsRepository;
    private BrailleTranslationClient brailleTranslationClient;

    @Autowired
    public BrailleTranslateEventListener(
            final TranslationsRepository translationsRepository,
            final BrailleTranslationClient brailleTranslationClient
    ) {
        this.translationsRepository = translationsRepository;
        this.brailleTranslationClient = brailleTranslationClient;
    }

    @Async
    @TransactionalEventListener
    @Transactional(propagation = REQUIRES_NEW)
    public void downloadLatexFile(final BrailleTranslateEvent event) {
        final Translations translations = translationsRepository.getById(event.getId());

        final File latexFile = FileUtil.findFile(translations.getLatexPath());

        if (!latexFile.exists()) {
            throw new FileException("파일이 존재하지 않습니다!");
        }

        final String brfContent = brailleTranslationClient.translate(latexFile);
        final String brfPath = FileUtil.saveFile(brfContent, translations.getOcrPdfId(), Paths.get("src", "main", "brf"));

        translations.finishTransbraille(brfPath);
    }
}
