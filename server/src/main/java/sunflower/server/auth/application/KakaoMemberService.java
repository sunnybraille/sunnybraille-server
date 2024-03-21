package sunflower.server.auth.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sunflower.server.auth.client.kakao.KakaoAccessTokenClient;
import sunflower.server.auth.client.kakao.KakaoUserProfileClient;
import sunflower.server.auth.client.response.KakaoAccessTokenResponse;
import sunflower.server.auth.client.response.KakaoUserProfileResponse;
import sunflower.server.entity.LoginType;
import sunflower.server.entity.Member;
import sunflower.server.repository.MemberRepository;

import java.util.Optional;

@Service
public class KakaoMemberService implements MemberOAuthService {

    private final String redirectURI;
    private final String restApiKey;
    private final String authCodeURI;
    private final String authTokenURI;
    private final String userProfileURI;
    private final KakaoAccessTokenClient kakaoAccessTokenClient;
    private final KakaoUserProfileClient kakaoUserProfileClient;
    private final MemberRepository memberRepository;

    public KakaoMemberService(
            @Value("${oauth.kakao.rest-api-key}") String restApiKey,
            @Value("${oauth.kakao.redirect-uri}") String redirectURI,
            @Value("${oauth.kakao.auth-code-uri}") String authCodeURI,
            @Value("${oauth.kakao.token-uri}") String authTokenURI,
            @Value("${oauth.kakao.user-info-request-uri}") String userProfileURI,
            final KakaoAccessTokenClient kakaoAccessTokenClient,
            final KakaoUserProfileClient kakaoUserProfileClient,
            final MemberRepository memberRepository
    ) {
        this.redirectURI = redirectURI;
        this.restApiKey = restApiKey;
        this.authCodeURI = authCodeURI;
        this.authTokenURI = authTokenURI;
        this.userProfileURI = userProfileURI;
        this.kakaoAccessTokenClient = kakaoAccessTokenClient;
        this.kakaoUserProfileClient = kakaoUserProfileClient;
        this.memberRepository = memberRepository;
    }

    public String loginURI() {
        return String.format(authCodeURI, restApiKey, redirectURI);
    }

    public Long login(final String code) {
        final KakaoAccessTokenResponse kakaoAccessTokenResponse = kakaoAccessTokenClient.requestAccessToken(authTokenURI, restApiKey, redirectURI, code);
        final String accessToken = kakaoAccessTokenResponse.getAccessToken();
        final KakaoUserProfileResponse kakaoUserProfileResponse = kakaoUserProfileClient.requestUserProfile(userProfileURI, accessToken);
        final Long oauthId = kakaoUserProfileResponse.getOauthId();
        final Member member = findOrCreateMember(kakaoUserProfileResponse, oauthId);
        return member.getId();
    }

    private Member findOrCreateMember(final KakaoUserProfileResponse kakaoUserProfileResponse, final Long oauthId) {
        final Optional<Member> findMember = memberRepository.findByLoginTypeAndOauthId(LoginType.KAKAO, oauthId);

        if (findMember.isPresent()) {
            return findMember.get();
        }

        return memberRepository.save(
                Member
                        .oauth()
                        .loginType(LoginType.KAKAO)
                        .nickname(kakaoUserProfileResponse.getNickname())
                        .oauthId(kakaoUserProfileResponse.getOauthId())
                        .build()
        );
    }
}
