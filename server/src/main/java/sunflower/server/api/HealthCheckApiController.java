package sunflower.server.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "서버 헬스 체크", description = "서버가 작동중인지 확인하는 API")
public class HealthCheckApiController {

    @Operation(summary = "헬스 체크 API", description = "서버가 작동중인지 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Server is running properly."),
            @ApiResponse(responseCode = "500", description = "Server is not running properly.")
    })
    @GetMapping("/health")
    public ResponseEntity<Void> healthCheck() {
        return ResponseEntity.ok().build();
    }
}
