package studioyoga.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import studioyoga.project.repository.EventRepository;
import studioyoga.project.repository.UserRepository;



@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

	 @Autowired
    private UserRepository userRepository;

	@Autowired
	private EventRepository eventRepository;

	@GetMapping("/dashboard")
	public String showAdminPanel(Model model) {
		long totalUsuarios = userRepository.count();
		long totalEventos = eventRepository.count();
		model.addAttribute("totalUsuarios", totalUsuarios);
		model.addAttribute("totalEventos", totalEventos);
		// model.addAttribute("totalClases", totalClases);
        // model.addAttribute("reservasPendientes", reservasPendientes);
		return "admin/dashboard"; 
	}

}	