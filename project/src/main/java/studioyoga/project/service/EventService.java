package studioyoga.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import studioyoga.project.model.Event;
import studioyoga.project.repository.EventRepository;

/**
 * Servicio para la gestión de eventos.
 * Proporciona métodos para listar, buscar, crear, editar, eliminar y
 * activar/desactivar eventos.
 */
@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    /**
     * Obtiene la lista de eventos activos ordenados por fecha ascendente.
     *
     * @return Lista de eventos activos.
     */
    public List<Event> findAllActive() {
        return eventRepository.findByActiveTrueOrderByEventDateAsc();
    }

    /**
     * Obtiene la lista de todos los eventos.
     *
     * @return Lista de todos los eventos.
     */
    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    /**
     * Busca un evento por su ID.
     *
     * @param id ID del evento.
     * @return El evento si existe, o null si no se encuentra.
     */
    public Optional<Event> findById(Integer id) {
        return eventRepository.findById(id);
    }

    /**
     * Guarda o actualiza un evento.
     *
     * @param event Evento a guardar o actualizar.
     * @return El evento guardado.
     */
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    /**
     * Elimina un evento por su ID.
     *
     * @param id ID del evento a eliminar.
     */
    public void deleteById(Integer id) {
        eventRepository.deleteById(id);
    }

    /**
     * Cambia el estado de activo/inactivo de un evento.
     *
     * @param id ID del evento a modificar.
     */
    public void toggleActive(Integer id) {
        findById(id).ifPresent(event -> {
            event.setActive(!event.isActive());
            save(event);
        });
    }

}