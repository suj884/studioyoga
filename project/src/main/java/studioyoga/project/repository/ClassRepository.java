package studioyoga.project.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import studioyoga.project.model.Classes;

public interface ClassRepository extends JpaRepository<Classes, Integer> {
  List<Classes> findByEventDateAfterOrderByEventDateAsc(LocalDate eventDate);

  List<Classes> findByActiveTrueOrderByEventDateAsc();

  @Query("SELECT COUNT(c) FROM Classes c WHERE c.eventDate = :eventDate AND c.timeInit = :timeInit")
int countByEventDateAndTimeInit(@Param("eventDate") LocalDate eventDate, @Param("timeInit") LocalTime timeInit);

@Query("SELECT COUNT(c) FROM Classes c WHERE c.eventDate = :eventDate AND c.timeInit = :timeInit AND c.id <> :id")
int countByEventDateAndTimeInitAndIdNot(LocalDate eventDate, LocalTime timeInit, Integer id);


 

}
