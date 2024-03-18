package sunflower.server.auth.api;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import sunflower.server.api.request.JoinRequest;
import sunflower.server.api.request.LoginRequest;

@Tag(name = "회원가입/로그인 API", description = "Sunny Braille 회원 관련 API로, 점역 기능 사용을 위해 필요합니다.")
public interface MemberApiControllerDocs {

    @Operation(summary = "회원가입 API", description = "요청한 아이디, 비밀번호는 데이터베이스에 저장됩니다. (비밀번호는 암호화됩니다)")
    @ApiResponse(responseCode = "200", description = "회원가입이 완료되었습니다. 다시  로그인해 주세요")
    ResponseEntity<Void> join(@RequestBody JoinRequest request);

    @Operation(summary = "로그인 API", description = "회원가입 시 입력했던 아이디, 비밀번호를 전송해 주세요.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sessionId", value = "세션 식별자", required = true, dataType = "string", paramType = "cookie")
    })
    ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletResponse response);
}
