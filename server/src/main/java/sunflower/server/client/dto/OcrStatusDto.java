package sunflower.server.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OcrStatusDto {

    @JsonProperty("status")
    private String status;

    @JsonProperty("input_file")
    private String inputFileName;

    @JsonProperty("percent_done")
    private Integer percentDone;

    public OcrStatus getStatus() {
        return OcrStatus.from(this.status);
    }
}
