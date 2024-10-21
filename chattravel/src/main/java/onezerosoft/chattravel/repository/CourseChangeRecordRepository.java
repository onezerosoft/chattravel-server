package onezerosoft.chattravel.repository;

import jakarta.transaction.Transactional;
import onezerosoft.chattravel.domain.CourseChangeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Transactional
@Repository
public interface CourseChangeRecordRepository extends JpaRepository<CourseChangeRecord, Long> {
    @Override
    Optional<CourseChangeRecord> findById(Long aLong);
}
