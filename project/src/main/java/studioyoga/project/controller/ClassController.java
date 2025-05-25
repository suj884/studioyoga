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

@Controller
@RequestMapping("/admin/classes")
public class ClassController {

    @Autowired
    private ClassesService classesService;

    @GetMapping("/manageclasses")
    public String manageClasses(Model model) {
        List<Classes> classesList = classesService.findAll();
        model.addAttribute("classesList", classesList);
        return "admin/manageclasses";
    }

    // Mostrar formulario de creación
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("classes", new Classes());
        return "admin/formClasses";
    }

    // Guardar nueva clase o editar existente
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

    // Mostrar formulario de edición
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Classes classes = classesService.findById(id).orElseThrow(() -> new RuntimeException("Clase no encontrada"));
        model.addAttribute("classes", classes);
        return "admin/formClasses";
    }

    // Eliminar clase
    @GetMapping("/delete/{id}")
    public String deleteClass(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        classesService.deleteClassAndReservations(id);
        redirectAttributes.addFlashAttribute("success", "Clase eliminada correctamente y todas las reservas asociadas");
        return "redirect:/admin/classes/manageclasses";
    }

}
