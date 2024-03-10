package sunflower.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sunflower.server.entity.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {
}
