package sunflower.server.auth.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sunflower.server.auth.client.response.KakaoUserProfileResponse;

@Slf4j
@Component
public class KakaoUserProfileClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public KakaoUserProfileClient(
            final RestTemplate restTemplate,
            final ObjectMapper objectMapper
    ) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public KakaoUserProfileResponse requestUserProfile(
            final String requestURI,
            final String accessToken
    ) {
        HttpHeaders requestHeader = createRequestHeader(accessToken);
        final MultiValueMap<String, Object> requestBody = createRequestBody();
        final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, requestHeader);

        log.info("Request URI: {}", requestURI);
        log.info("Request Headers: {}", requestHeader);

        final ResponseEntity<String> response = restTemplate.exchange(requestURI, HttpMethod.GET, requestEntity, String.class);

        final String responseBody = response.getBody();
        System.out.println("responseBody = " + responseBody);

        try {
            final JsonNode responseJson = objectMapper.readTree(responseBody);
            final long id = responseJson.path("id").asLong();
            final String nickname = responseJson.path("properties").path("nickname").asText();
            return new KakaoUserProfileResponse(id, nickname);
        } catch (Exception e) {
            log.error("Error parsing JSON response: {}", e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    private HttpHeaders createRequestHeader(final String accessToken) {
        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        requestHeader.set(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
        return requestHeader;
    }

    private MultiValueMap<String, Object> createRequestBody() {
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("property_keys", "[\"kakao_account.email\"]");
        return requestBody;
    }
}
