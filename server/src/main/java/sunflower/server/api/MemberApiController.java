package sunflower.server.api;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sunflower.server.api.request.JoinRequest;
import sunflower.server.api.request.LoginRequest;
import sunflower.server.application.MemberService;
import sunflower.server.application.SessionService;

@RequiredArgsConstructor
@RestController
public class MemberApiController implements MemberApiControllerDocs {

    private final MemberService memberService;
    private final SessionService sessionService;

    @Override
    @PostMapping("/join")
    public ResponseEntity<Void> join(@RequestBody JoinRequest request) {
        memberService.join(request.getLoginId(), request.getPassword());

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .build();
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        final Long id = memberService.login(request.getLoginId(), request.getPassword());
        final String sessionId = sessionService.createSessionId(id);

//        Cookie cookie = new Cookie("sessionId", sessionId);
//        cookie.setHttpOnly(true);
//        cookie.setMaxAge(3600);
//        cookie.setPath("/");
//        cookie.setSecure(true);
//        response.addCookie(cookie);

        response.setHeader("Set-Cookie", "sessionId=" + sessionId + "; HttpOnly; Max-Age=3600; Path=/; Domain=sunnybraille.com; Secure; SameSite=None");

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .build();
    }
}
