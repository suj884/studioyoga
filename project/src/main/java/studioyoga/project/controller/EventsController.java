package studioyoga.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import studioyoga.project.model.Event;
import studioyoga.project.service.EventService;



@Controller
@RequestMapping("/admin/events")
public class EventsController {

	    @Autowired
	    private EventService eventService;
	    
	 // Listar eventos
	    @GetMapping("/manageevents")
	    public String manageEvents(Model model) {
	        model.addAttribute("events", eventService.findAll());
	        return "admin/manageevents";
	    }

	    // Mostrar formulario de creación
	    @GetMapping("/create")
	    public String showCreateForm(Model model) {
	        model.addAttribute("event", new Event());
	        return "admin/formEvents";
	    }

	    // Guardar evento (nuevo o editado)
	    @PostMapping("/save")
	    public String saveEvent(@ModelAttribute Event event) {
	        eventService.save(event);
	        return "redirect:/admin/events/manageevents";
	    }

	    // Mostrar formulario de edición
	    @GetMapping("/edit/{id}")
	    public String showEditForm(@PathVariable Integer id, Model model) {
	        model.addAttribute("event", eventService.findById(id));
	        return "admin/formEvents";
	    }

	    // Eliminar evento
	    @GetMapping("/delete/{id}")
	    public String deleteEvent(@PathVariable Integer id) {
	        eventService.deleteById(id);
	        return "redirect:/admin/manageevents";
	    }

	    // Activar/desactivar evento
	    @GetMapping("/toggle/{id}")
	    public String toggleActive(@PathVariable Integer id) {
	        eventService.toggleActive(id);
	        return "redirect:/admin/events/manageevents";
	    }
	}




