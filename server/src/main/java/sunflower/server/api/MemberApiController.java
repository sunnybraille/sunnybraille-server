package sunflower.server.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sunflower.server.api.request.LoginRequest;
import sunflower.server.api.response.AuthTokensResponse;
import sunflower.server.application.MemberService;
import sunflower.server.util.AuthTokensGenerator;

@RequiredArgsConstructor
@RestController
public class MemberApiController {

    private final MemberService memberService;
    private final AuthTokensGenerator authTokensGenerator;

    @PostMapping("/login")
    ResponseEntity<AuthTokensResponse> login(@RequestBody LoginRequest request) {
        final Long id = memberService.login(request.getLoginId(), request.getPassword());
        return ResponseEntity.ok(authTokensGenerator.generateToken(id));
    }
}
