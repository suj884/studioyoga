package studioyoga.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import studioyoga.project.model.Event;
import studioyoga.project.repository.EventRepository;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

//    public List<Event> getUpcomingEvents(LocalDate now) {
//        return eventRepository.findByEventDateGreaterThanEqualOrderByEventDateAsc(now);
//    }
    public List<Event> findAllActive() { return eventRepository.findByActiveTrue(); }
    public List<Event> findAll() { return eventRepository.findAll(); }
    public Event findById(Integer id) { return eventRepository.findById(id).orElse(null); }
    public Event save(Event event) { return eventRepository.save(event); }
    public void deleteById(Integer id) { eventRepository.deleteById(id); }
    public void toggleActive(Integer id) {
        Event event = findById(id);
        if (event != null) {
            event.setActive(!event.isActive());
            save(event);
        }
    }
}

