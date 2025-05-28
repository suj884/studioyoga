package studioyoga.project.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import studioyoga.project.model.Classes;

/**
 * Repositorio para la entidad {@link Classes}.
 * Proporciona métodos para acceder y consultar clases en la base de datos.
 */
public interface ClassRepository extends JpaRepository<Classes, Integer> {

    /**
     * Busca las clases cuya fecha de evento es posterior a la fecha proporcionada,
     * ordenadas de forma ascendente por fecha de evento.
     *
     * @param eventDate Fecha a partir de la cual buscar las clases.
     * @return Lista de clases futuras ordenadas por fecha.
     */
    List<Classes> findByEventDateAfterOrderByEventDateAsc(LocalDate eventDate);

    /**
     * Busca todas las clases activas y las ordena de forma ascendente por fecha de evento.
     *
     * @return Lista de clases activas ordenadas por fecha.
     */
    List<Classes> findByActiveTrueOrderByEventDateAsc();

    /**
     * Cuenta cuántas clases existen en una fecha y hora de inicio específicas.
     *
     * @param eventDate Fecha del evento.
     * @param timeInit Hora de inicio de la clase.
     * @return Número de clases en esa fecha y hora.
     */
    @Query("SELECT COUNT(c) FROM Classes c WHERE c.eventDate = :eventDate AND c.timeInit = :timeInit")
    int countByEventDateAndTimeInit(@Param("eventDate") LocalDate eventDate, @Param("timeInit") LocalTime timeInit);

    /**
     * Cuenta cuántas clases existen en una fecha y hora de inicio específicas,
     * excluyendo una clase con un ID determinado.
     *
     * @param eventDate Fecha del evento.
     * @param timeInit Hora de inicio de la clase.
     * @param id ID de la clase a excluir.
     * @return Número de clases en esa fecha y hora, excluyendo la clase con el ID dado.
     */
    @Query("SELECT COUNT(c) FROM Classes c WHERE c.eventDate = :eventDate AND c.timeInit = :timeInit AND c.id <> :id")
    int countByEventDateAndTimeInitAndIdNot(LocalDate eventDate, LocalTime timeInit, Integer id);

   
}