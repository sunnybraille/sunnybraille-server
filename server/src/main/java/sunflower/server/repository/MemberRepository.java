package sunflower.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sunflower.server.entity.Member;

import java.util.NoSuchElementException;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    default Member getByLoginId(String loginId) {
        final Optional<Member> member = findByLoginId(loginId);
        if (member.isEmpty()) {
            throw new NoSuchElementException("Member with login id " + loginId + " not found");
        }
        return member.get();
    }

    Optional<Member> findByLoginId(String loginId);
}
