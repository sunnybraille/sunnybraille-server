package sunflower.server.auth.client.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KakaoUserProfileResponse {

    private final long oauthId;
    private final String nickname;
}
