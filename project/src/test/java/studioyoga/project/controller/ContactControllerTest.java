package studioyoga.project.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mail.javamail.JavaMailSender;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContactController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class ContactControllerTest {

     @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JavaMailSender mailSender;

    @Test
    void testSendContact() throws Exception {
        mockMvc.perform(post("/contact")
                .param("nombre", "Juan")
                .param("email", "juan@example.com")
                .param("mensaje", "Hola, esto es una prueba"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/formContact"));
    }
}
