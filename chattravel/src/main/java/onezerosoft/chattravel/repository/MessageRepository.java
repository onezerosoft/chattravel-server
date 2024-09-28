package onezerosoft.chattravel.repository;

import onezerosoft.chattravel.domain.Course;
import onezerosoft.chattravel.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Repository

public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<Message> findById(Long Id);
}
