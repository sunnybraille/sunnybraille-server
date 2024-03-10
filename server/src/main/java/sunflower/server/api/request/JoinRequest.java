package sunflower.server.api.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class JoinRequest {

    private final String loginId;
    private final String password;
}
