package studioyoga.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import studioyoga.project.model.Reservation;
import studioyoga.project.model.User;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByUser(User user);
    List<Reservation> findByUserId(Integer userId);
    List<Reservation> findByClassesId(Integer classId);
    Optional<Reservation> findByIdAndUser(Integer id, User user);
    List<Reservation> findByUserNameContainingIgnoreCase(String userName);
    List<Reservation> findByClassesTitleContainingIgnoreCase(String title);
    List<Reservation> findByUserNameContainingIgnoreCaseAndClassesTitleContainingIgnoreCase(String userName, String title);
    @Transactional
    void deleteByClassesId(Integer classId);
}
