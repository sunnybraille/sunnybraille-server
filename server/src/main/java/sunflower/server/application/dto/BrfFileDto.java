package sunflower.server.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sunflower.server.entity.Translations;

@RequiredArgsConstructor
@Getter
public class BrfFileDto {

    private final Long id;
    private final String originalFileName;
    private final String content;

    public static BrfFileDto of(final Translations translations, final String content) {
        return new BrfFileDto(translations.getId(), translations.getInputFileName(), content);
    }
}
