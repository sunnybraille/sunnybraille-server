package sunflower.server.auth.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sunflower.server.auth.client.KakaoOAuthClient;

@Service
public class KakaoMemberService {

    private final String redirectURI;
    private final String restApiKey;
    private final String authCodeURI;
    private final String authTokenURI;
    private final KakaoOAuthClient kakaoOAuthClient;

    public KakaoMemberService(
            @Value("${oauth.kakao.rest-api-key}") String restApiKey,
            @Value("${oauth.kakao.redirect-uri}") String redirectURI,
            @Value("${oauth.kakao.auth-code-uri}") String authCodeURI,
            @Value("${oauth.kakao.token-uri}") String authTokenURI,
            final KakaoOAuthClient kakaoOAuthClient
    ) {
        this.redirectURI = redirectURI;
        this.restApiKey = restApiKey;
        this.authCodeURI = authCodeURI;
        this.authTokenURI = authTokenURI;
        this.kakaoOAuthClient = kakaoOAuthClient;
    }

    public String loginURI() {
        return String.format(authCodeURI, restApiKey, redirectURI);
    }

    public String login(final String code) {
        kakaoOAuthClient.login(authTokenURI, restApiKey, redirectURI, code);
        return null;
    }
}
