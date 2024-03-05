package sunflower.server.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sunflower.server.api.response.AuthTokensResponse;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class AuthTokensGenerator {

    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24;       // 24時間
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  //　七日間

    private final JwtTokenProvider jwtTokenProvider;

    public AuthTokensResponse generateToken(Long memberId) {
        final long now = (new Date()).getTime();
        return AuthTokensResponse.of(
                generateToken(memberId, now + ACCESS_TOKEN_EXPIRE_TIME),
                generateToken(memberId, now + REFRESH_TOKEN_EXPIRE_TIME),
                BEARER_TYPE,
                ACCESS_TOKEN_EXPIRE_TIME / 1000L);
    }

    private String generateToken(final Long memberId, long expiredAt) {
        return jwtTokenProvider.generate(memberId.toString(), new Date(expiredAt));
    }

    public boolean isValidToken(String token) {
        return jwtTokenProvider.isValidToken(token);
    }

    public Long extractMemberId(String accessToken) {
        return Long.valueOf(jwtTokenProvider.extractSubject(accessToken));
    }
}
