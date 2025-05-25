package studioyoga.project.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import studioyoga.project.model.Event;

/**
 * Repositorio para la entidad {@link Event}.
 * Proporciona métodos para acceder y consultar eventos en la base de datos.
 */
public interface EventRepository extends JpaRepository<Event, Integer> {

    /**
     * Busca los eventos cuya fecha es igual o posterior a la fecha proporcionada,
     * ordenados de forma ascendente por fecha de evento.
     *
     * @param now Fecha a partir de la cual buscar los eventos.
     * @return Lista de eventos futuros o en la fecha indicada.
     */
    List<Event> findByEventDateGreaterThanEqualOrderByEventDateAsc(LocalDate now);

    /**
     * Busca todos los eventos que están activos.
     *
     * @return Lista de eventos activos.
     */
    List<Event> findByActiveTrue();

    /**
     * Busca todos los eventos activos y los ordena de forma ascendente por fecha de evento.
     *
     * @return Lista de eventos activos ordenados por fecha.
     */
    List<Event> findByActiveTrueOrderByEventDateAsc();
}