package sunflower.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sunflower.server.entity.Translations;

public interface TranslationsRepository extends JpaRepository<Translations, Long> {
}
