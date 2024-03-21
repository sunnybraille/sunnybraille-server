package sunflower.server.auth.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sunflower.server.repository.MemberRepository;

@Service
public class GoogleMemberService {

    private final String redirectURI;
    private final String loginRedirectURI;
    private final String clientId;
    private final String scope;
    private final String clientSecret;
    private final MemberRepository memberRepository;

    public GoogleMemberService(
            @Value("${oauth.google.redirect-uri}") String redirectURI,
            @Value("${oauth.google.login-uri}") String loginRedirectURI,
            @Value("${oauth.google.client-id}") String clientId,
            @Value("${oauth.google.scope}") String scope,
            @Value("${oauth.google.client-secret}") String clientSecret,
            final MemberRepository memberRepository
    ) {
        this.redirectURI = redirectURI;
        this.loginRedirectURI = loginRedirectURI;
        this.clientId = clientId;
        this.scope = scope;
        this.clientSecret = clientSecret;
        this.memberRepository = memberRepository;
    }

    public String loginURI() {
        return String.format(loginRedirectURI, clientId, redirectURI, scope);
    }
}
