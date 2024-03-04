package sunflower.server.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import sunflower.server.api.response.BrfFileQueryResponse;
import sunflower.server.api.response.PdfRegisterResponse;
import sunflower.server.api.response.TranslationStatusResponse;

@Tag(name = "점자 번역 API", description = "Sunny Braille 핵심 기능으로, 점역 작업을 담당합니다.")
public interface TranslationApiControllerDocs {

    @Operation(summary = "PDF 점자 번역 요청 API", description = "점역 작업은 서버에서 비동기적으로 처리되며, id만 즉시 반환됩니다.")
    @ApiResponse(responseCode = "201", description = "PDF가 성공적으로 업로드되었습니다.",
            headers =
            @io.swagger.v3.oas.annotations.headers.Header(
                    name = "Location",
                    description = "Translations id(해바라기에서 제공하는 id)",
                    schema = @Schema(type = "long"),
                    required = true
            ))
    ResponseEntity<PdfRegisterResponse> registerPdf(
            @Parameter(name = "file", description = "점역할 pdf 파일", required = true,
                    example = "쎈_3124번.pdf", schema = @Schema(type = "file"))
            @RequestPart("file") MultipartFile file
    );

    @Operation(summary = "점역 상황 체크 API", description = "id와 함께 요청하면 점역 진행 상황을 반환하며, 클라이언트의 progress bar 표시를 위해 사용해 주세요.")
    @ApiResponse(responseCode = "200", description = "점역 진행 상황을 반환합니다.",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TranslationStatusResponse.class))
            })
    ResponseEntity<TranslationStatusResponse> checkStatus(
            @Parameter(description = "Translations id", required = true) @PathVariable("id") Long id);

    @Operation(summary = "점역 결과 BRF 파일 반환 API", description = "id와 함께 요청하면 점역 결과를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "점역된 결과를 반환합니다..",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BrfFileQueryResponse.class))
            })
    ResponseEntity<BrfFileQueryResponse> queryBrfFile(
            @Parameter(description = "Translations id", required = true) @PathVariable("id") Long id);
}
