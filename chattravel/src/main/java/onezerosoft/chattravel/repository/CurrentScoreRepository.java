package onezerosoft.chattravel.repository;

import jakarta.transaction.Transactional;
import onezerosoft.chattravel.domain.CurrentScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Transactional
@Repository
public interface CurrentScoreRepository extends JpaRepository<CurrentScore, Long> {
    CurrentScore findFirstByOrderByCreatedAtDesc();

    CurrentScore findFirstByOrderByIdDesc();

}
