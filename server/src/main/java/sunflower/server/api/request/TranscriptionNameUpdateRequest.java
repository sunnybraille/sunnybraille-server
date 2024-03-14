package sunflower.server.api.request;

import lombok.Getter;

@Getter
public class TranscriptionNameUpdateRequest {

    private String name;

    public TranscriptionNameUpdateRequest() {
    }

    public TranscriptionNameUpdateRequest(final String name) {
        this.name = name;
    }
}
