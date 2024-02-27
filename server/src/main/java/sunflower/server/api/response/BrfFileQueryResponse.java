package sunflower.server.api.response;

import lombok.Getter;

@Getter
public class BrfFileQueryResponse {

    private final Long id;
    private final String content;

    public BrfFileQueryResponse(final Long id, final String content) {
        this.id = id;
        this.content = content;
    }

    public static BrfFileQueryResponse from(final Long id, final String brfContent) {
        return new BrfFileQueryResponse(id, brfContent);
    }
}
