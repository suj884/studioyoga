package studioyoga.project.service;

import org.springframework.stereotype.Service;

import studioyoga.project.model.Ticket;
import studioyoga.project.repository.TicketRepository;

import java.util.List;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public void changeStatus(Long id, String nuevoEstado) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow();
        ticket.setEstado(nuevoEstado);
        ticketRepository.save(ticket);
    }
}
