package studioyoga.project.controller;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import studioyoga.project.model.BlogPost;
import studioyoga.project.model.Event;
import studioyoga.project.service.BlogService;
import studioyoga.project.service.EventService;
import studioyoga.project.service.GuideSectionService;



@Controller
public class HomeController {

	@Autowired
	private EventService eventService;
	@Autowired
	// private ClassesService classsService;
	
	private final GuideSectionService guideSectionService;
	 @Autowired
    private BlogService blogService;

    public HomeController(GuideSectionService guideSectionService) {
        this.guideSectionService = guideSectionService;
    }

	@GetMapping("/")
	public String landingPage(Model model) {
		model.addAttribute("events", eventService.findAllActive());
		return "user/index";
	}

	@GetMapping("/login")
	public String login() {
		return "user/login";
	}

	@GetMapping("/formRegister")
	public String register() {
		return "user/formRegister";
	}
	@GetMapping("/schedule")
	public String schedule() {
		return "user/schedule";
	}


	@GetMapping("/prices")
	public String prices() {
		return "user/prices";
	}

	@GetMapping("/rules")
	public String rules() {
		return "user/rules";
	}

	@GetMapping("/events")
	public String events(Model model) {
		List<Event> events = eventService.findAll();
		model.addAttribute("events", events);
		return "user/events";
	}
       // Listado de posts para usuarios
    @GetMapping("/blog")
    public String showBlog(Model model) {
List<BlogPost> blogPosts = blogService.findAllPublishedOrdered();
        model.addAttribute("blogPosts", blogPosts);
        return "user/blog";
    }

    // Detalle de un post
    @GetMapping("/blog/{id}")
    public String showBlogPostDetail(@PathVariable Integer id, Model model) {
        BlogPost post = blogService.findById(id)
                .orElseThrow(() -> new RuntimeException("Post no encontrado"));
        model.addAttribute("post", post);
        return "user/blogPostDetail";
    }
	@GetMapping("/guide")
    public String showYogaGuide(Model model) {
        model.addAttribute("sections", guideSectionService.getAllSections());
        return "user/guide"; 
    }

	@GetMapping("/faq")
	public String faq() {
		return "user/faq";
	}

	@GetMapping("/location")
	public String location() {
		return "user/location";
	}

}