package sunflower.server.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sunflower.server.entity.Session;
import sunflower.server.repository.SessionRepository;
import sunflower.server.util.SessionEncryptor;

@RequiredArgsConstructor
@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final SessionEncryptor sessionEncryptor;

    public String createSessionId(final Long memberId) {
        final Session session = sessionRepository.save(Session.of(memberId));
        return sessionEncryptor.encrypt(session.getId());
    }
}
