package onezerosoft.chattravel.repository;

import onezerosoft.chattravel.domain.Chat;
import onezerosoft.chattravel.domain.enums.ChatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findById(Long Id);

    List<Chat> findByStatus(ChatStatus status);
}
