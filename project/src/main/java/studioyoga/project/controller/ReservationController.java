package studioyoga.project.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import studioyoga.project.model.Reservation;
import studioyoga.project.service.ClassesService;
import studioyoga.project.service.ReservationService;
import studioyoga.project.service.UserService;

@Controller
@RequestMapping("/admin/reservations")
public class ReservationController {

     @Autowired
    private ReservationService reservationService;

    @Autowired
    private ClassesService classesService;

    @Autowired
    private UserService userService;

    // Listar reservas
    @GetMapping("/managereservations")
    public String manageReservations(
        @RequestParam(required = false) String user,
        @RequestParam(required = false) String className,
        @RequestParam(required = false) Boolean all,
        Model model
    ) {
        List<Reservation> reservations;
        boolean searchPerformed = false;

        if (Boolean.TRUE.equals(all)) {
            reservations = reservationService.findAll();
            searchPerformed = true;
        } else if ((user != null && !user.isEmpty()) || (className != null && !className.isEmpty())) {
            reservations = reservationService.findByUserOrClass(user, className);
            searchPerformed = true;
        } else {
            // Mostrar todas por defecto
            reservations = reservationService.findAll();
        }

        model.addAttribute("reservations", reservations);
        model.addAttribute("searchPerformed", searchPerformed);

        if (searchPerformed && user != null && user.isEmpty()) {
            model.addAttribute("info", "No existen usuarios con ese criterio");
        }
        if (searchPerformed && reservations.isEmpty()) {
            model.addAttribute("info", "No existen reservas con ese nombre");
        }

        return "admin/managereservations";
    }

    // Mostrar formulario de creación
    @GetMapping("/createReservation")
    public String showCreateForm(Model model) {
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("classes", classesService.findAll());
        model.addAttribute("users", userService.findAll());
        return "admin/formReservation";
    }
    // Guardar reserva
    @PostMapping("/save")
    public String saveReservation(@ModelAttribute Reservation reservation, RedirectAttributes redirectAttributes) {
        reservation.setDateReservation(LocalDateTime.now());
        reservationService.save(reservation);
        redirectAttributes.addFlashAttribute("message", "Reserva guardada correctamente");
        return "redirect:/admin/managereservations";
    }
    // Mostrar formulario de edición
    @GetMapping("/editReservation/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("reservation", reservationService.findById(id));
        model.addAttribute("classes", classesService.findAll());
        model.addAttribute("users", userService.findAll());
        return "admin/formReservation";
    }
    // Eliminar reserva
    @GetMapping("/deleteReservation/{id}")
    public String deleteReservation(@PathVariable Integer id) {
        reservationService.deleteById(id);
        return "redirect:/admin/managereservations";
    }
    // Activar/desactivar reserva
    @GetMapping("/toggleReservation/{id}")
    public String toggleActive(@PathVariable Integer id) {
        reservationService.toggleActive(id);
        return "redirect:/admin/reservations/managereservations";
    }
//     Método para obtener las reservas de un usuario específico
//     @GetMapping("/userReservations/{userId}")
//     public String getUserReservations(@PathVariable Integer userId, Model model) {
//         model.addAttribute("reservations", reservationService.findByUserId(userId));
//         return "admin/userReservations";
//     }
//     Método para obtener las reservas de una clase específica
//     @GetMapping("/classReservations/{classId}")
//     public String getClassReservations(@PathVariable Integer classId, Model model) {
//         model.addAttribute("reservations", reservationService.findByClassesId(classId));
//         return "admin/classReservations";
//     }
}
