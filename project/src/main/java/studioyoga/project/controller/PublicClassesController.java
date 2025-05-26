package studioyoga.project.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import studioyoga.project.dto.ClassesDTO;
import studioyoga.project.exception.AlreadyReservedException;
import studioyoga.project.exception.NoSpotsAvailableException;
import studioyoga.project.model.User;
import studioyoga.project.service.ClassesService;
import studioyoga.project.service.ReservationService;
import studioyoga.project.service.UserService;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/classes")
public class PublicClassesController {
    @Autowired
    private ClassesService classesService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public String showClasses(Model model) {
        List<ClassesDTO> classesList = classesService.findUpcomingClassesWithSpots();
        System.out.println("Clases encontradas: " + classesList.size());
        for (ClassesDTO dto : classesList) {
            System.out.println(dto.getClasses().getEventDate() + " - " + dto.getClasses().getTitle());
        }

        Map<LocalDate, List<ClassesDTO>> clasesPorFecha = classesList.stream()
                .collect(Collectors.groupingBy(dto -> dto.getClasses().getEventDate(), LinkedHashMap::new,
                        Collectors.toList()));
        model.addAttribute("clasesPorFecha", clasesPorFecha);

        return "user/classes";
    }
     @PostMapping("/reserve/{id}")
    public String reserveClass(@PathVariable Integer id, Principal principal, RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(principal.getName());
        try {
            reservationService.reserveClass(user, id);
            redirectAttributes.addFlashAttribute("success", "Reserva realizada correctamente.");
        } catch (NoSpotsAvailableException e) {
            redirectAttributes.addFlashAttribute("error", "No hay plazas disponibles para esta clase.");
        } catch (AlreadyReservedException e) {
            redirectAttributes.addFlashAttribute("error", "Ya has reservado esta clase.");
        }
        return "redirect:/classes";
    }

    @GetMapping("/myReservations")
    public String myReservations(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("reservations", reservationService.findByUser(user));
        return "user/myReservations";
    }

    @PostMapping("/cancelReservation/{id}")
    public String cancelReservation(@PathVariable Integer id, Principal principal,
            RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByEmail(principal.getName());
        try {
            reservationService.cancelReservation(user, id);
            redirectAttributes.addFlashAttribute("success", "Reserva cancelada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No es posible cancelar la reserva.");
        }
        return "redirect:/classes/myReservations";
    }

    @GetMapping("/confirm-cancel/{id}")
    public String confirmCancelReservation(@PathVariable Integer id, Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByEmail(principal.getName());
        var reservation = reservationService.findByUserAndId(user, id);
        if (reservation == null) {
            // Puedes redirigir con un mensaje de error si la reserva no existe o no
            // pertenece al usuario
            return "redirect:/classes/myReservations";
        }
        model.addAttribute("reservation", reservation);
        return "user/confirmCancelReservation"; // Tu plantilla de confirmación
    }

}
