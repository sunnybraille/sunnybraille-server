package sunflower.server.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sunflower.server.entity.Transcriptions;

@RequiredArgsConstructor
@Getter
public class BrfFileDto {

    private final Long id;
    private final String name;
    private final String originalFileName;
    private final String content;

    public static BrfFileDto of(final Transcriptions transcriptions, final String content) {
        return new BrfFileDto(transcriptions.getId(), transcriptions.getName(), transcriptions.getInputFileName(), content);
    }
}
