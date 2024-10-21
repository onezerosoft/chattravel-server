package onezerosoft.chattravel.repository;

import jakarta.transaction.Transactional;
import onezerosoft.chattravel.domain.UserReactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Transactional
@Repository
public interface UserReactionRecordRepository extends JpaRepository<UserReactionRecord, Long> {
    Optional<UserReactionRecord> findByMessageId();
}
