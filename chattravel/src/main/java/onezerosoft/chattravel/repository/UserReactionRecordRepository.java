package onezerosoft.chattravel.repository;

import jakarta.transaction.Transactional;
import onezerosoft.chattravel.domain.UserReactionRecord;
import onezerosoft.chattravel.domain.enums.UserReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Transactional
@Repository
public interface UserReactionRecordRepository extends JpaRepository<UserReactionRecord, Long> {
    Optional<UserReactionRecord> findByMessageIdAndIsValid(Integer messageId, String isValid);

    long countByIsValidAndUserReaction(String isValid, UserReaction userReaction);

}
