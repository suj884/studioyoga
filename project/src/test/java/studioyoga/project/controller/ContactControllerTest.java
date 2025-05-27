package studioyoga.project.controller;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import studioyoga.project.service.BlogService;
import studioyoga.project.service.EventService;
import studioyoga.project.service.GuideSectionService;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Collections;
import java.util.Optional;

@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventService eventService;

    @MockitoBean
    private BlogService blogService;

    @MockitoBean
    private GuideSectionService guideSectionService;

    @Test
    @DisplayName("GET / debe mostrar la página principal")
    void testLandingPage() throws Exception {
        when(eventService.findAllActive()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/index"))
                .andExpect(model().attributeExists("events"));
    }

    @Test
    @DisplayName("GET /login debe mostrar la vista de login")
    void testLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/login"));
    }

    @Test
    @DisplayName("GET /formRegister debe mostrar el formulario de registro")
    void testRegister() throws Exception {
        mockMvc.perform(get("/formRegister"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/formRegister"));
    }

    @Test
    @DisplayName("GET /schedule debe mostrar la página de horarios")
    void testSchedule() throws Exception {
        mockMvc.perform(get("/schedule"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/schedule"));
    }

    @Test
    @DisplayName("GET /prices debe mostrar la página de precios")
    void testPrices() throws Exception {
        mockMvc.perform(get("/prices"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/prices"));
    }

    @Test
    @DisplayName("GET /rules debe mostrar la página de reglas")
    void testRules() throws Exception {
        mockMvc.perform(get("/rules"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/rules"));
    }

    @Test
    @DisplayName("GET /events muestra la lista de eventos")
    void testEvents() throws Exception {
        when(eventService.findAllActive()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/events"))
                .andExpect(model().attributeExists("events"));
    }

    @Test
    @DisplayName("GET /blog muestra la lista de publicaciones")
    void testShowBlog() throws Exception {
        when(blogService.findAllPublishedOrdered()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/blog"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/blog"))
                .andExpect(model().attributeExists("blogPosts"));
    }

    @Test
    @DisplayName("GET /blog/{id} muestra el detalle de una publicación")
    void testShowBlogPostDetail() throws Exception {
        int postId = 1;
        var dummyPost = new studioyoga.project.model.BlogPost();
        when(blogService.findById(postId)).thenReturn(Optional.of(dummyPost));
        mockMvc.perform(get("/blog/{id}", postId))
                .andExpect(status().isOk())
                .andExpect(view().name("user/blogPostDetail"))
                .andExpect(model().attributeExists("post"));
    }

    @Test
    @DisplayName("GET /guide muestra la guía de yoga")
    void testShowYogaGuide() throws Exception {
        when(guideSectionService.getAllSections()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/guide"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/guide"))
                .andExpect(model().attributeExists("sections"));
    }

    @Test
    @DisplayName("GET /faq muestra la página de preguntas frecuentes")
    void testFaq() throws Exception {
        mockMvc.perform(get("/faq"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/faq"));
    }

    @Test
    @DisplayName("GET /location muestra la página de ubicación")
    void testLocation() throws Exception {
        mockMvc.perform(get("/location"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/location"));
    }

    @Test
    @DisplayName("GET /aboutUS muestra la página sobre nosotros")
    void testAboutUs() throws Exception {
        mockMvc.perform(get("/aboutUS"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/aboutUs"));
    }

    @Test
    @DisplayName("GET /formContact muestra el formulario de contacto")
    void testContact() throws Exception {
        mockMvc.perform(get("/formContact"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/formContact"));
    }
}
