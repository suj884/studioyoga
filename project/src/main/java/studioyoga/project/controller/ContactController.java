package studioyoga.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para la gestión del formulario de contacto.
 * Permite enviar mensajes de contacto por correo electrónico al administrador
 * del sitio.
 */
@Controller
public class ContactController {

    @Autowired
    private JavaMailSender mailSender;


     /**
     * Muestra el formulario de contacto para que el usuario pueda enviar un mensaje.
     *
     * @return Nombre de la vista del formulario de contacto.
     */
    @GetMapping("/contact")
    public String showContactForm() {
        return "formContact";
    }

    /**
     * Procesa el envío del formulario de contacto y envía un correo electrónico al
     * administrador.
     *
     * @param nombre             Nombre del remitente.
     * @param email              Email del remitente.
     * @param mensaje            Mensaje enviado por el usuario.
     * @param redirectAttributes Atributos para mensajes flash en la redirección.
     * @return Redirección a la página de contacto con mensaje de éxito.
     */
    @PostMapping("/contact")
    public String sendContact(
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String mensaje,
            RedirectAttributes redirectAttributes) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo("studioyogasevilla@gmail.com");
        mailMessage.setSubject("Nuevo mensaje de contacto de " + nombre);
        mailMessage.setText("Nombre: " + nombre + "\nEmail: " + email + "\n\nMensaje:\n" + mensaje);

        mailSender.send(mailMessage);
        redirectAttributes.addFlashAttribute("success",
                "¡Mensaje enviado correctamente! Nos pondremos en contacto contigo pronto.");
        return "redirect:/formContact";
    }
}