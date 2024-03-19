package sunflower.server.auth.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class KakaoUserProfileClient {

    private final RestTemplate restTemplate;

    public KakaoUserProfileClient(
            final RestTemplate restTemplate
    ) {
        this.restTemplate = restTemplate;
    }

    public void requestUserProfile(
            final String requestURI,
            final String accessToken
    ) {
        HttpHeaders requestHeader = createRequestHeader(accessToken);
        final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(null, requestHeader);

        log.info("Request URI: {}", requestURI);
        log.info("Request Headers: {}", requestHeader);

        final ResponseEntity<String> response = restTemplate.exchange(requestURI, HttpMethod.GET, requestEntity, String.class);

        final String responseBody = response.getBody();
        System.out.println("responseBody = " + responseBody);
    }

    private HttpHeaders createRequestHeader(final String accessToken) {
        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        requestHeader.set(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
        return requestHeader;
    }
}
