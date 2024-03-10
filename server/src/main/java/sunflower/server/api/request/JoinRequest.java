package sunflower.server.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@ApiModel(description = "회원 가입 요청")
public class JoinRequest {

    @ApiModelProperty(value = "로그인 아이디", example = "gitchan", required = true)
    private final String loginId;

    @ApiModelProperty(value = "비밀번호", example = "gitchan123", required = true)
    private final String password;
}
