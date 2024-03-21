package sunflower.server.auth.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sunflower.server.auth.application.GoogleMemberService;

@RequiredArgsConstructor
@RestController
public class GoogleMemberApiController {

    private final GoogleMemberService googleMemberService;

    @GetMapping("/login/google")
    public ResponseEntity<Void> loginRedirectURI() {
        final String uri = googleMemberService.loginURI();
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", uri)
                .build();
    }
}
