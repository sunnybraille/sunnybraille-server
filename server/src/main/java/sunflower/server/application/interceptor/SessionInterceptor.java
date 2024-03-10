package sunflower.server.application.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import sunflower.server.application.SessionService;
import sunflower.server.exception.AuthException;
import sunflower.server.util.SessionEncryptor;

@Component
@RequiredArgsConstructor
public class SessionInterceptor implements HandlerInterceptor {

    private final SessionService sessionService;
    private final SessionEncryptor sessionEncryptor;

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) {
        String encryptedSessionId = extractSessionId(request.getCookies());
        if (encryptedSessionId == null) {
            throw new AuthException();
        }

        final Long sessionId = sessionEncryptor.decrypt(encryptedSessionId);
        if (sessionService.isValidSession(sessionId)) {
            return true;
        } else {
            throw new AuthException();
        }
    }

    private String extractSessionId(final Cookie[] cookies) {
        if (cookies == null) {
            throw new AuthException();
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("sessionId")) {
                return cookie.getValue();
            }
        }
        throw new AuthException();
    }
}
