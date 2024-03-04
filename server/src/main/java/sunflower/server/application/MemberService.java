package sunflower.server.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sunflower.server.entity.Member;
import sunflower.server.repository.MemberRepository;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Long login(final String loginId, final String password) {
        final Member member = memberRepository.getByLoginId(loginId);
        member.checkPassword(password);
        return member.getId();
    }
}
