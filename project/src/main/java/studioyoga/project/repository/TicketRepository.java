package studioyoga.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import studioyoga.project.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    // Puedes añadir métodos de consulta si lo necesitas
}