package studioyoga.project.controller;

import java.util.List;
import java.util.Optional;

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

/**
 * Controlador para la gestión de las vistas públicas y principales del sitio web.
 * Permite mostrar la página de inicio, login, registro, horarios, precios, reglas,
 * eventos, blog, guía de yoga, preguntas frecuentes, ubicación, información sobre nosotros y contacto.
 */
@Controller
public class HomeController {

    @Autowired
    private EventService eventService;

    // private ClassesService classsService;

    private final GuideSectionService guideSectionService;

    @Autowired
    private BlogService blogService;

    /**
     * Constructor que inyecta el servicio de secciones de la guía.
     *
     * @param guideSectionService Servicio de secciones de la guía.
     */
    public HomeController(GuideSectionService guideSectionService) {
        this.guideSectionService = guideSectionService;
    }

    /**
     * Muestra la página principal con los eventos activos.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return Vista de la página de inicio.
     */
    @GetMapping("/")
    public String landingPage(Model model) {
        model.addAttribute("events", eventService.findAllActive());
        return "user/index";
    }

    /**
     * Muestra la página de login.
     *
     * @return Vista de login.
     */
    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    /**
     * Muestra el formulario de registro de usuario.
     *
     * @return Vista del formulario de registro.
     */
    @GetMapping("/formRegister")
    public String register() {
        return "user/formRegister";
    }

    /**
     * Muestra la página de horarios.
     *
     * @return Vista de horarios.
     */
    @GetMapping("/schedule")
    public String schedule() {
        return "user/schedule";
    }

    /**
     * Muestra la página de precios.
     *
     * @return Vista de precios.
     */
    @GetMapping("/prices")
    public String prices() {
        return "user/prices";
    }

    /**
     * Muestra la página de reglas.
     *
     * @return Vista de reglas.
     */
    @GetMapping("/rules")
    public String rules() {
        return "user/rules";
    }

    /**
     * Muestra la lista de eventos activos.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return Vista de eventos.
     */
    @GetMapping("/events")
    public String events(Model model) {
        List<Event> events = eventService.findAllActive();
        model.addAttribute("events", events);
        return "user/events";
    }
@GetMapping("/event-detail/{id}")
public String eventDetail(@PathVariable Integer id, Model model) {
    Optional<Event> eventOpt = eventService.findById(id); // O como accedas a tus eventos
    if (eventOpt.isPresent()) {
        model.addAttribute("event", eventOpt.get());
        return "user/eventsDetail"; 
    } else {
        return "redirect:/eventos?error=notfound";
    }
}
    /**
     * Muestra la lista de publicaciones del blog para los usuarios.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return Vista del blog.
     */
    @GetMapping("/blog")
    public String showBlog(Model model) {
        List<BlogPost> blogPosts = blogService.findAllPublishedOrdered();
        model.addAttribute("blogPosts", blogPosts);
        return "user/blog";
    }

    /**
     * Muestra el detalle de una publicación del blog.
     *
     * @param id ID de la publicación.
     * @param model Modelo para pasar datos a la vista.
     * @return Vista de detalle de la publicación del blog.
     */
    @GetMapping("/blog/{id}")
    public String showBlogPostDetail(@PathVariable Integer id, Model model) {
        BlogPost post = blogService.findById(id)
                .orElseThrow(() -> new RuntimeException("Post no encontrado"));
        model.addAttribute("post", post);
        return "user/blogPostDetail";
    }

    /**
     * Muestra la guía de yoga.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return Vista de la guía de yoga.
     */
    @GetMapping("/guide")
    public String showYogaGuide(Model model) {
        model.addAttribute("sections", guideSectionService.getAllSections());
        return "user/guide";
    }

    /**
     * Muestra la página de preguntas frecuentes.
     *
     * @return Vista de preguntas frecuentes.
     */
    @GetMapping("/faq")
    public String faq() {
        return "user/faq";
    }

    /**
     * Muestra la página de ubicación.
     *
     * @return Vista de ubicación.
     */
    @GetMapping("/location")
    public String location() {
        return "user/location";
    }

    /**
     * Muestra la página de información sobre nosotros.
     *
     * @return Vista sobre nosotros.
     */
    @GetMapping("/aboutUS")
    public String aboutUs() {
        return "user/aboutUs";
    }

    /**
     * Muestra el formulario de contacto.
     *
     * @return Vista del formulario de contacto.
     */
    @GetMapping("/formContact")
    public String contact() {
        return "user/formContact";
    }

}