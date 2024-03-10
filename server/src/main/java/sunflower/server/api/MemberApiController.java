package sunflower.server.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sunflower.server.api.request.JoinRequest;
import sunflower.server.api.request.LoginRequest;
import sunflower.server.application.MemberService;

@RequiredArgsConstructor
@RestController
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/join")
    ResponseEntity<Void> join(@RequestBody JoinRequest request) {
        memberService.join(request.getLoginId(), request.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED.value()).build();
    }

    @PostMapping("/login")
    ResponseEntity<Void> login(@RequestBody LoginRequest request) {
        final Long id = memberService.login(request.getLoginId(), request.getPassword());
        return null;
    }
}
