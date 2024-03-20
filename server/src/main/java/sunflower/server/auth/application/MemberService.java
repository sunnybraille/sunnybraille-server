package sunflower.server.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sunflower.server.entity.Member;
import sunflower.server.repository.MemberRepository;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Long join(final String loginId, final String password) {
        final Member member = Member
                .basicLogin()
                .loginId(loginId)
                .password(password)
                .build();
        return memberRepository.save(member).getId();
    }

    public Long login(final String loginId, final String password) {
        final Member member = memberRepository.getByLoginId(loginId);
        member.checkPassword(password);
        return member.getId();
    }
}
