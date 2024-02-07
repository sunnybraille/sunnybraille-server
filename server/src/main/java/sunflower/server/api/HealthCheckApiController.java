package sunflower.server.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class HealthCheckApiController {

    @RequestMapping(method = GET, path = "/health")
    public ResponseEntity<Void> healthCheck() {
        return ResponseEntity.ok().build();
    }
}
