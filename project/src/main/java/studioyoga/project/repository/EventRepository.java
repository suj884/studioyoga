package studioyoga.project.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import studioyoga.project.model.Event;

public interface EventRepository extends JpaRepository<Event, Integer> {

	List<Event> findByEventDateGreaterThanEqualOrderByEventDateAsc(LocalDate now);
	List<Event> findByActiveTrue();

}
