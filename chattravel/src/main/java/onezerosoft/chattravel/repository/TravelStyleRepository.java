package onezerosoft.chattravel.repository;

import onezerosoft.chattravel.domain.Travel;
import onezerosoft.chattravel.domain.TravelStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Repository
public interface TravelStyleRepository extends JpaRepository<TravelStyle, Long> {
    Optional<TravelStyle> findById(Long Id);
}
