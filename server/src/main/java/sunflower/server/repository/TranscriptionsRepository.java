package sunflower.server.repository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import sunflower.server.entity.Transcriptions;

public interface TranscriptionsRepository extends JpaRepository<Transcriptions, Long> {

    default Transcriptions getById(final Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transcriptions with id " + id + " not found"));
    }
}
