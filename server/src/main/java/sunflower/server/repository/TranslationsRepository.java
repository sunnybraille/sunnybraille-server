package sunflower.server.repository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import sunflower.server.entity.Translations;

public interface TranslationsRepository extends JpaRepository<Translations, Long> {

    default Translations getById(final Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Translation with id " + id + " not found"));
    }
}
