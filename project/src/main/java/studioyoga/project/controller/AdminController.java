package studioyoga.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import studioyoga.project.repository.BlogPostRepository;
import studioyoga.project.repository.ClassRepository;
import studioyoga.project.repository.EventRepository;
import studioyoga.project.repository.GuideSectionRepository;
import studioyoga.project.repository.ReservationRepository;
import studioyoga.project.repository.UserRepository;



@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

	 @Autowired
    private UserRepository userRepository;

	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private BlogPostRepository blogPostRepository;
	@Autowired
	private ClassRepository classRepository;
	@Autowired
	private ReservationRepository reservationRepository;
	@Autowired
	private GuideSectionRepository guideSectionRepository;

	@GetMapping("/dashboard")
	public String showAdminPanel(Model model) {
		long totalClass = classRepository.count();
		long totalEvents = eventRepository.count();
		long totalUsers = userRepository.count();
		long totalReservations = reservationRepository.count();
		long totalGuideSections = guideSectionRepository.count();
		long totalBlog = blogPostRepository.count();
		model.addAttribute("totalUsers", totalUsers);
		model.addAttribute("totalEvents", totalEvents);
		model.addAttribute("totalClass", totalClass);
        model.addAttribute("totalReservations", totalReservations);
		model.addAttribute("totalGuideSections", totalGuideSections);
		model.addAttribute("totalBlog", totalBlog);
		return "admin/dashboard"; 
	}

}	