package sunflower.server.application.resolver;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import sunflower.server.application.SessionService;
import sunflower.server.exception.AuthException;
import sunflower.server.util.SessionEncryptor;

@RequiredArgsConstructor
@Component
public class MemberAuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final SessionService sessionService;
    private final SessionEncryptor sessionEncryptor;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter
                .getParameterType()
                .equals(MemberAuth.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String encryptedSessionId = findEncryptedSessionId(request);

        if (encryptedSessionId == null) {
            throw new AuthException();
        }

        final Long sessionId = sessionEncryptor.decrypt(encryptedSessionId);
        if (sessionService.isValidSession(sessionId)) {
            final Long memberId = sessionService.findMemberIdBy(sessionId);
            sessionService.extendTime(sessionId);
            return MemberAuth.from(memberId);
        } else {
            throw new AuthException();
        }
    }

    private String findEncryptedSessionId(final HttpServletRequest request) {
        final String sessionId = request.getHeader("SessionId");
        if (sessionId != null) {
            return sessionId;
        }

        return extractSessionId(request.getCookies());
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
