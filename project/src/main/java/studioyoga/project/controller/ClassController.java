package studioyoga.project.controller;

import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import studioyoga.project.model.Classes;
import studioyoga.project.service.ClassesService;

/**
 * Controlador para la gestión de clases en el panel de administración.
 * Permite listar, crear, editar y eliminar clases, así como validar horarios y
 * evitar duplicados.
 */
@Controller
@RequestMapping("/admin/classes")
public class ClassController {

    @Autowired
    private ClassesService classesService;

    /**
     * Muestra la lista de todas las clases registradas.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return Vista de administración de clases.
     */
    @GetMapping("/manageclasses")
    public String manageClasses(Model model) {
        List<Classes> classesList = classesService.findAll();
        model.addAttribute("classesList", classesList);
        return "admin/manageclasses";
    }

    /**
     * Muestra el formulario para crear una nueva clase.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return Vista del formulario de creación de clase.
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("classes", new Classes());
        return "admin/formClasses";
    }

    /**
     * Guarda una nueva clase o actualiza una existente, validando que no haya
     * conflictos de horario ni duplicados.
     *
     * @param classes            Objeto Classes a guardar.
     * @param redirectAttributes Atributos para mensajes flash en la redirección.
     * @return Redirección a la vista de administración de clases o al formulario en
     *         caso de error.
     */
    @PostMapping("/save")
    public String saveClass(@ModelAttribute("classes") Classes classes, RedirectAttributes redirectAttributes) {
        String dayOfWeek = classes.getEventDate().getDayOfWeek()
                .getDisplayName(TextStyle.FULL, new Locale("es")).toUpperCase();
        String time = classes.getTimeInit().format(DateTimeFormatter.ofPattern("HH:mm"));
        String className = classes.getTitle();
        if (!classesService.isAllowedSchedule(dayOfWeek, time, className)) {
            redirectAttributes.addFlashAttribute("error", "No puedes registrar esa clase en ese horario.");
            return "redirect:/admin/classes/new";
        }
        boolean exists;
        if (classes.getId() != null) {
            // Edición: excluir la propia clase
            exists = classesService.existsByDateTimeExcludingId(classes.getEventDate(), classes.getTimeInit(),
                    classes.getId());
        } else {
            // Creación: comprobar normalmente
            exists = classesService.existsByDateTime(classes.getEventDate(), classes.getTimeInit());
        }
        if (exists) {
            redirectAttributes.addFlashAttribute("error", "Ya existe una clase registrada en esa fecha y hora.");
            return "redirect:/admin/classes/new";
        }
        classesService.save(classes);
        redirectAttributes.addFlashAttribute("success", "Clase guardada correctamente");
        return "redirect:/admin/classes/manageclasses";
    }

    /**
     * Muestra el formulario de edición para una clase existente.
     *
     * @param id    ID de la clase a editar.
     * @param model Modelo para pasar datos a la vista.
     * @return Vista del formulario de edición de clase.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Classes classes = classesService.findById(id).orElseThrow(() -> new RuntimeException("Clase no encontrada"));
        model.addAttribute("classes", classes);
        return "admin/formClasses";
    }

    @PostMapping("/delete/{id}")
    public String deleteClass(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        classesService.deleteClassAndReservations(id);
        redirectAttributes.addFlashAttribute("success", "Clase eliminada correctamente y todas las reservas asociadas");
        return "redirect:/admin/classes/manageclasses";
    }

    @GetMapping("/confirm-delete/{id}")
    public String confirmDelete(@PathVariable Integer id, Model model) {
        Classes clase = classesService.findById(id)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));

        // Si quieres mostrar el número de reservas asociadas:
        int numReservas = classesService.countReservationsForClass(id); // Implementa este método abajo

        String mensaje = "¿Seguro que quieres eliminar la clase: '" + clase.getTitle() + "'?";
        if (numReservas > 0) {
            mensaje += " Esta acción eliminará también las " + numReservas + " reservas asociadas.";
        } else {
            mensaje += " Esta acción eliminará todas las reservas asociadas (si existen).";
        }

        model.addAttribute("message", mensaje);
        model.addAttribute("action", "/admin/classes/delete/" + id);
        model.addAttribute("cancelUrl", "/admin/classes/manageclasses");
        return "admin/confirm-delete";
    }

}