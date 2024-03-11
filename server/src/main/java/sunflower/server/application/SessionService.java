package sunflower.server.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public boolean isValidSession(final Long sessionId) {
        final Session session = sessionRepository.getById(sessionId);
        return session.isValid();
    }

    public Long findMemberIdBy(final Long sessionId) {
        return sessionRepository
                .getById(sessionId)
                .getMemberId();
    }

    @Transactional
    public void extendTime(final Long sessionId) {
        final Session session = sessionRepository.getById(sessionId);
        session.extendTime();
    }
}
