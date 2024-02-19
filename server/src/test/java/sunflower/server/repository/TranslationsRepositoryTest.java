package sunflower.server.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sunflower.server.entity.Translations;

import java.io.File;

@DataJpaTest
class TranslationsRepositoryTest {

    @Autowired
    private TranslationsRepository translationsRepository;

    @Test
    void 점역_파일을_저장한다() {
        final Translations file = new Translations("gitchan_translated_23423jh", 30.0, new File(""), new File(""));
        final Translations savedFile = translationsRepository.save(file);

        Assertions.assertThat(file)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(savedFile);
    }
}
