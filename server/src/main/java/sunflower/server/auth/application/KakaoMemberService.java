package sunflower.server.auth.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sunflower.server.auth.client.KakaoAccessTokenClient;
import sunflower.server.auth.client.KakaoUserProfileClient;
import sunflower.server.auth.client.response.KakaoOAuthResponse;

@Service
public class KakaoMemberService {

    private final String redirectURI;
    private final String restApiKey;
    private final String authCodeURI;
    private final String authTokenURI;
    private final String userProfileURI;
    private final KakaoAccessTokenClient kakaoAccessTokenClient;
    private final KakaoUserProfileClient kakaoUserProfileClient;

    public KakaoMemberService(
            @Value("${oauth.kakao.rest-api-key}") String restApiKey,
            @Value("${oauth.kakao.redirect-uri}") String redirectURI,
            @Value("${oauth.kakao.auth-code-uri}") String authCodeURI,
            @Value("${oauth.kakao.token-uri}") String authTokenURI,
            @Value("${oauth.kakao.user-info-request-uri}") String userProfileURI,
            final KakaoAccessTokenClient kakaoAccessTokenClient,
            final KakaoUserProfileClient kakaoUserProfileClient
    ) {
        this.redirectURI = redirectURI;
        this.restApiKey = restApiKey;
        this.authCodeURI = authCodeURI;
        this.authTokenURI = authTokenURI;
        this.userProfileURI = userProfileURI;
        this.kakaoAccessTokenClient = kakaoAccessTokenClient;
        this.kakaoUserProfileClient = kakaoUserProfileClient;
    }

    public String loginURI() {
        return String.format(authCodeURI, restApiKey, redirectURI);
    }

    public String login(final String code) {
        final KakaoOAuthResponse response = kakaoAccessTokenClient.requestAccessToken(authTokenURI, restApiKey, redirectURI, code);
        final String accessToken = response.getAccessToken();
        kakaoUserProfileClient.requestUserProfile(userProfileURI, accessToken);

        return null;
    }
}
