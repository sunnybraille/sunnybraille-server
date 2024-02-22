package sunflower.server.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sunflower.server.api.response.TranslationStatusResponse;
import sunflower.server.application.TranslationService;
import sunflower.server.application.dto.TranslationStatusDto;
import sunflower.server.exception.FileEmptyException;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/translations")
public class TranslationApiController {

    private final TranslationService translationService;

    @PostMapping
    public ResponseEntity<Void> registerPdf(@RequestPart("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileEmptyException(1, "빈 파일입니다.");
        }

        final Long id = translationService.register(file);

        return ResponseEntity
                .created(URI.create("/translations/" + id))
                .build();
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<TranslationStatusResponse> checkStatus(@PathVariable("id") Long id) {
        final TranslationStatusDto dto = translationService.status(id);
        return ResponseEntity.ok(TranslationStatusResponse.from(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Void> downloadBrfFile(@PathVariable("id") Long id) {
        return null;
    }
}

