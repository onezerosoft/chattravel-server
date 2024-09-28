package onezerosoft.chattravel.repository;

import onezerosoft.chattravel.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findById(Long Id);
}
