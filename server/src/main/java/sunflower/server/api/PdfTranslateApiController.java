package sunflower.server.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sunflower.server.api.response.TranslationStatusResponse;
import sunflower.server.application.PdfTranslationService;
import sunflower.server.application.dto.TranslationStatusDto;
import sunflower.server.exception.FileEmptyException;

import java.net.URI;

@RequiredArgsConstructor
@RestController
public class PdfTranslateApiController {

    private final PdfTranslationService pdfTranslationService;

    @PostMapping("/translate-pdf/new")
    public ResponseEntity<Void> registerPdf(@RequestPart("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileEmptyException(1, "빈 파일입니다.");
        }

        final Long id = pdfTranslationService.register(file);

        return ResponseEntity
                .created(URI.create("/translations/" + id))
                .build();
    }

    @GetMapping("/translations/{id}/status")
    public ResponseEntity<TranslationStatusResponse> checkStatus(@PathVariable("id") Long id) {
        final TranslationStatusDto dto = pdfTranslationService.status(id);
        return ResponseEntity.ok(TranslationStatusResponse.from(dto));
    }

    @PostMapping("/translate-pdf")
    public ResponseEntity<Void> translatePdf(@RequestPart("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileEmptyException(1, "빈 파일입니다.");
        }

        try {
            final Long location = pdfTranslationService.translate(file);
            return ResponseEntity
                    .created(URI.create("/translations/" + location))
                    .build();
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }
}

