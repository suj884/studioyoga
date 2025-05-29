package studioyoga.project.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import studioyoga.project.constants.RedirConstants;
import studioyoga.project.model.Event;
import studioyoga.project.service.EventService;

/**
 * Controlador para la gestión de eventos en el panel de administración.
 * Permite listar, crear, editar, guardar, eliminar y activar/desactivar
 * eventos.
 */
@Controller
@RequestMapping("/admin/events")
public class EventsController {

    @Autowired
    private EventService eventService;

    /**
     * Muestra la lista de eventos para administración.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return Vista de administración de eventos.
     */
    @GetMapping("/manageevents")
    public String manageEvents(Model model) {
        model.addAttribute("events", eventService.findAll());
        return "admin/manageevents";
    }

    /**
     * Muestra el formulario para crear un nuevo evento.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return Vista del formulario de creación de evento.
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("event", new Event());
        return "admin/formEvents";
    }

    /**
     * Guarda un evento nuevo o editado.
     *
     * @param event Objeto Event a guardar.
     * @return Redirección a la vista de administración de eventos.
     */
    @PostMapping("/save")
    public String saveEvent(@ModelAttribute Event event, RedirectAttributes redirectAttributes) {
        eventService.save(event);
        redirectAttributes.addFlashAttribute("success", "Evento guardado correctamente.");
        return RedirConstants.REDIRECT_ADMIN_EVENTS;
    }

@GetMapping("/edit/{id}")
public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
    Optional<Event> eventOpt = eventService.findById(id);
    if (!eventOpt.isPresent()) {
        redirectAttributes.addFlashAttribute("error", "Evento no encontrado");
        return RedirConstants.REDIRECT_ADMIN_EVENTS;
    }
    model.addAttribute("event", eventOpt.get());
    return "admin/formEvents";
}


    /**
     * Elimina un evento por su ID.
     *
     * @param id ID del evento a eliminar.
     * @return Redirección a la vista de administración de eventos.
     */
    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Integer id) {
        eventService.deleteById(id);
        return "redirect:/admin/manageevents";
    }

    /**
     * Activa o desactiva un evento (cambia su estado activo).
     *
     * @param id ID del evento a activar o desactivar.
     * @return Redirección a la vista de administración de eventos.
     */
    @GetMapping("/toggle/{id}")
    public String toggleActive(@PathVariable Integer id) {
        eventService.toggleActive(id);
        return "redirect:/admin/events/manageevents";
    }

    @GetMapping("/confirm-delete/{id}")
    public String confirmDelete(@PathVariable Integer id, Model model) {
        Event event = eventService.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
        model.addAttribute("message", "¿Seguro que quieres eliminar el evento: '" + event.getTitle() + "'?");
        model.addAttribute("action", "/admin/events/delete/" + id);
        model.addAttribute("cancelUrl", "/admin/events/manageevents");
        return "admin/confirm-delete";
    }

    // Eliminar el evento (POST)
    @PostMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        eventService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Evento eliminado correctamente.");
        return "redirect:/admin/events/manageevents";
    }

}