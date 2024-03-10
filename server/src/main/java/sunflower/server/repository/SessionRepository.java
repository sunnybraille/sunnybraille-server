package sunflower.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sunflower.server.entity.Session;
import sunflower.server.exception.AuthException;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    default Session getById(final Long id) {
        final Optional<Session> session = findById(id);
        if (session.isEmpty()) {
            new AuthException();
        }
        return session.get();
    }
}
