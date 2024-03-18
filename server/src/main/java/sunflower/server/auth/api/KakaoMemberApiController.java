package sunflower.server.auth.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sunflower.server.application.SessionService;
import sunflower.server.auth.application.KakaoMemberService;

@RequiredArgsConstructor
@RestController
public class KakaoMemberApiController {

    private final KakaoMemberService kakaoMemberService;
    private final SessionService sessionService;

    @GetMapping("/login/kakao")
    public ResponseEntity<Void> login() {
        final String uri = kakaoMemberService.loginURI();
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", uri)
                .build();
    }

    @GetMapping("/login/kakao/session")
    public ResponseEntity<Void> authorize(@RequestParam("code") String code) {
        kakaoMemberService.login(code);
        return null;
    }
}
