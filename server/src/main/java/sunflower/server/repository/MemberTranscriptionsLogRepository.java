package sunflower.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sunflower.server.entity.MemberTranscriptionsLog;

import java.time.LocalDate;
import java.util.Optional;

public interface MemberTranscriptionsLogRepository extends JpaRepository<MemberTranscriptionsLog, Long> {

    Optional<MemberTranscriptionsLog> findByMemberId(final Long memberId);

    Optional<MemberTranscriptionsLog> findByMemberIdAndDate(Long memberId, LocalDate today);
}
