package studioyoga.project.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(HomeController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false) // Desactiva filtros de seguridad

class HomeControllerTest {

       @Autowired
    private MockMvc mockMvc;

    @org.springframework.boot.test.mock.mockito.MockBean
    private studioyoga.project.service.EventService eventService;

    @org.springframework.boot.test.mock.mockito.MockBean
    private studioyoga.project.service.BlogService blogService;

    @org.springframework.boot.test.mock.mockito.MockBean
    private studioyoga.project.service.GuideSectionService guideSectionService;
    
    @BeforeEach
    void setUp() {
        // Configuración inicial si es necesaria
    }

    @Test
    void testLandingPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/index"));
    }

    @Test
    void testLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/login"));
    }

    @Test
    void testRegisterPage() throws Exception {
        mockMvc.perform(get("/formRegister"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/formRegister"));
    }

    @Test
    void testSchedulePage() throws Exception {
        mockMvc.perform(get("/schedule"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/schedule"));
    }

    @Test
    void testPricesPage() throws Exception {
        mockMvc.perform(get("/prices"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/prices"));
    }

    @Test
    void testRulesPage() throws Exception {
        mockMvc.perform(get("/rules"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/rules"));
    }

    @Test
    void testEventsPage() throws Exception {
        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/events"));
    }

    @Test
    void testBlogPage() throws Exception {
        mockMvc.perform(get("/blog"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/blog"));
    }

    @Test
    void testGuidePage() throws Exception {
        mockMvc.perform(get("/guide"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/guide"));
    }

    @Test
    void testFaqPage() throws Exception {
        mockMvc.perform(get("/faq"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/faq"));
    }

    @Test
    void testLocationPage() throws Exception {
        mockMvc.perform(get("/location"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/location"));
    }
}