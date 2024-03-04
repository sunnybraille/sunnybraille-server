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
import sunflower.server.api.response.BrfFileQueryResponse;
import sunflower.server.api.response.PdfRegisterResponse;
import sunflower.server.api.response.TranslationStatusResponse;
import sunflower.server.application.TranslationService;
import sunflower.server.application.dto.TranslationStatusDto;
import sunflower.server.exception.FileEmptyException;

import java.net.URI;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping("/translations")
public class TranslationApiController implements TranslationApiControllerDocs {

    private final TranslationService translationService;

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PdfRegisterResponse> registerPdf(@RequestPart("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileEmptyException(1, "빈 파일입니다.");
        }

        final Long id = translationService.register(file);

        return ResponseEntity
                .created(URI.create("/translations/" + id))
                .body(PdfRegisterResponse.from(file.getOriginalFilename()));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<TranslationStatusResponse> checkStatus(@PathVariable("id") Long id) {
        final TranslationStatusDto dto = translationService.status(id);
        return ResponseEntity.ok(TranslationStatusResponse.from(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrfFileQueryResponse> queryBrfFile(@PathVariable("id") Long id) {
        final String brfContent = translationService.findBrfFileById(id);
        final BrfFileQueryResponse response = BrfFileQueryResponse.from(id, brfContent);
        return ResponseEntity.ok(response);
    }
}
