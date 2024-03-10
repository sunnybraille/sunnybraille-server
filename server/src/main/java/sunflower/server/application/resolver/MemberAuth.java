package sunflower.server.application.resolver;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MemberAuth {

    private final Long id;

    public static MemberAuth from(final Long memberId) {
        return new MemberAuth(memberId);
    }
}
