package onezerosoft.chattravel.repository;

import onezerosoft.chattravel.domain.Course;
import onezerosoft.chattravel.domain.Travel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {
    Optional<Travel> findById(Long Id);
}
