package sunflower.server.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sunflower.server.entity.MemberTranscriptionsLog;
import sunflower.server.repository.MemberTranscriptionsLogRepository;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberTranscriptionsLogService {

    private final MemberTranscriptionsLogRepository memberTranscriptionsLogRepository;

    public int count(final Long memberId) {
        final Optional<MemberTranscriptionsLog> log = memberTranscriptionsLogRepository.findByMemberIdAndDate(memberId, LocalDate.now());
        if (log.isEmpty()) {
            final MemberTranscriptionsLog newLog = memberTranscriptionsLogRepository.save(MemberTranscriptionsLog.of(memberId));
            return newLog.count();
        }
        return log.get().count();
    }
}
