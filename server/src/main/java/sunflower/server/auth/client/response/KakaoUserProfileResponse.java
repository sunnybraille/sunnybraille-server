package sunflower.server.auth.client.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KakaoUserProfileResponse {

    private final String nickname;
    private final Long oauthId;
}
