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

/**
 * Controlador para la gestión pública de clases.
 * Permite a los usuarios ver clases disponibles, reservar, consultar y cancelar sus reservas.
 */
@Controller
@RequestMapping("/classes")
public class PublicClassesController {

    @Autowired
    private ClassesService classesService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReservationService reservationService;

    /**
     * Muestra la lista de clases próximas agrupadas por fecha.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return Vista con las clases disponibles para los usuarios.
     */
    @GetMapping
    public String showClasses(Model model) {
        List<ClassesDTO> classesList = classesService.findUpcomingClassesWithSpots();
        Map<LocalDate, List<ClassesDTO>> clasesPorFecha = classesList.stream()
                .collect(Collectors.groupingBy(dto -> dto.getClasses().getEventDate(), LinkedHashMap::new,
                        Collectors.toList()));
        model.addAttribute("clasesPorFecha", clasesPorFecha);
        return "user/classes";
    }

    /**
     * Permite a un usuario reservar una clase.
     *
     * @param id ID de la clase a reservar.
     * @param principal Usuario autenticado.
     * @param redirectAttributes Atributos para mensajes flash en la redirección.
     * @return Redirección a la vista de clases con mensaje de éxito o error.
     */
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

    /**
     * Muestra las reservas del usuario autenticado.
     *
     * @param model Modelo para pasar datos a la vista.
     * @param principal Usuario autenticado.
     * @return Vista con las reservas del usuario o redirección al login si no está autenticado.
     */
    @GetMapping("/myReservations")
    public String myReservations(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("reservations", reservationService.findByUser(user));
        return "user/myReservations";
    }

    /**
     * Permite a un usuario cancelar una reserva.
     *
     * @param id ID de la reserva a cancelar.
     * @param principal Usuario autenticado.
     * @param redirectAttributes Atributos para mensajes flash en la redirección.
     * @return Redirección a la vista de reservas del usuario o al login si no está autenticado.
     */
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

    /**
     * Muestra una página de confirmación para cancelar una reserva.
     *
     * @param id ID de la reserva a cancelar.
     * @param principal Usuario autenticado.
     * @param model Modelo para pasar datos a la vista.
     * @return Vista de confirmación de cancelación o redirección si no corresponde.
     */
    @GetMapping("/confirm-cancel/{id}")
    public String confirmCancelReservation(@PathVariable Integer id, Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByEmail(principal.getName());
        var reservation = reservationService.findByUserAndId(user, id);
        if (reservation == null) {
            return "redirect:/classes/myReservations";
        }
        model.addAttribute("reservation", reservation);
        return "user/confirmCancelReservation";
    }

}