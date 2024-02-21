package sunflower.server.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OcrProgressDto {

    @JsonProperty("status")
    private final OcrProgressStatus status;

    @JsonProperty("input_file")
    private final String inputFileName;

    @JsonProperty("percent_done")
    private final Integer percentDone;
}
