package sunflower.server.auth.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KakaoMemberService {

    private String redirectURI;
    private String restApiKey;
    private String authCodeURI;

    public KakaoMemberService(
            @Value("${oauth.kakao.rest-api-key}") String restApiKey,
            @Value("${oauth.kakao.redirect-uri}") String redirectURI,
            @Value("${oauth.kakao.auth-code-uri}") String authCodeURI
    ) {
        this.redirectURI = redirectURI;
        this.restApiKey = restApiKey;
        this.authCodeURI = authCodeURI;
    }

    public String loginURI() {
        return String.format(authCodeURI, restApiKey, redirectURI);
    }
}
