package onezerosoft.chattravel.repository;

import onezerosoft.chattravel.domain.Chat;
import onezerosoft.chattravel.domain.Course;
import onezerosoft.chattravel.domain.Message;
import onezerosoft.chattravel.domain.enums.SendType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository

public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<Message> findById(Long Id);

    List<Message> findByChatAndType(Chat chat, SendType type);
}
