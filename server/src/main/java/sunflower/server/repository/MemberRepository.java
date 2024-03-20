package sunflower.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sunflower.server.entity.LoginType;
import sunflower.server.entity.Member;

import java.util.NoSuchElementException;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    default Member getByLoginId(final String loginId) {
        final Optional<Member> member = findByLoginId(loginId);
        if (member.isEmpty()) {
            throw new NoSuchElementException("Member with login id " + loginId + " not found");
        }
        return member.get();
    }

    Optional<Member> findByLoginId(final String loginId);

    default Member getByLoginTypeAndOauthId(final LoginType loginType, final Long oauthId) {
        final Optional<Member> member = findByLoginTypeAndOauthId(loginType, oauthId);
        if (member.isEmpty()) {
            throw new NoSuchElementException("Member with login type, oauthId " + loginType.name() + oauthId + " not found");
        }
        return member.get();
    }

    Optional<Member> findByLoginTypeAndOauthId(final LoginType loginType, final Long oauthId);
}
