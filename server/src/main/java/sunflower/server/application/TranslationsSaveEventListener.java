package sunflower.server.application;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import sunflower.server.application.event.TranslationsSaveEvent;
import sunflower.server.client.OcrDownloadClient;
import sunflower.server.client.OcrProgressClient;
import sunflower.server.client.OcrRequestClient;
import sunflower.server.entity.Translations;
import sunflower.server.repository.TranslationsRepository;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Component
public class TranslationsSaveEventListener {

    private TranslationsRepository translationsRepository;
    private OcrRequestClient ocrRequestClient;
    private OcrProgressClient ocrProgressClient;
    private OcrDownloadClient ocrDownloadClient;

    @TransactionalEventListener
    @Async
    public void handleTranslationsSave(final TranslationsSaveEvent event) {
        final Translations translations = event.getTranslations();
        System.out.println("translations.getId() = " + translations.getId());
        final String pdfURI = translations.getPdfURI();
        final File file = Paths.get(pdfURI).toFile();

        translations.startOcr();
        final MultipartFile multipartFile = convertToMultipartFile(file);
        final String pdfId = ocrRequestClient.requestPdfId(multipartFile);
        translations.setOcrPdfId(pdfId);
    }

    private MultipartFile convertToMultipartFile(File file) {
        if (file == null) {
            log.warn("file is empty!");
        }
        try {
            FileInputStream input = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            input.read(bytes);
            MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            map.add("file", new MultipartFileWrapper(bytes, file.getName()));
            return (MultipartFile) map.get("file").get(0);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class MultipartFileWrapper implements MultipartFile {
        private final byte[] bytes;
        private final String fileName;

        public MultipartFileWrapper(byte[] bytes, String fileName) {
            this.bytes = bytes;
            this.fileName = fileName;
        }

        @Override
        public String getName() {
            return fileName;
        }

        @Override
        public String getOriginalFilename() {
            return fileName;
        }

        @Override
        public String getContentType() {
            return MediaType.APPLICATION_PDF_VALUE;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public long getSize() {
            return bytes.length;
        }

        @Override
        public byte[] getBytes() {
            return bytes;
        }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(bytes);
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {
            new FileOutputStream(dest).write(bytes);
        }
    }
}
