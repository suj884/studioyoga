package studioyoga.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import studioyoga.project.model.GuideSection;
import studioyoga.project.service.GuideSectionService;

/**
 * Controlador para la gestión de secciones de la guía en el panel de administración.
 * Permite listar, crear, editar y eliminar secciones de la guía.
 */
@Controller
@RequestMapping("/admin/guide")
public class GuideController {

    // Servicio para la gestión de secciones de la guía
    private final GuideSectionService guideSectionService;

    /**
     * Constructor que inyecta el servicio de secciones de la guía.
     *
     * @param guideSectionService Servicio de secciones de la guía.
     */
    public GuideController(GuideSectionService guideSectionService) {
        this.guideSectionService = guideSectionService;
    }

    /**
     * Muestra la lista de todas las secciones de la guía.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return Vista de administración de secciones de la guía.
     */
    @GetMapping("manageguide")
    public String listGuideSections(Model model) {
        model.addAttribute("sections", guideSectionService.getAllSections());
        return "admin/manageguide";
    }

    /**
     * Muestra el formulario para crear una nueva sección de la guía.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return Vista del formulario de creación de sección.
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("guideSection", new GuideSection());
        return "admin/guideSectionForm";
    }

    /**
     * Procesa el formulario de creación de una nueva sección de la guía.
     *
     * @param section Objeto GuideSection a guardar.
     * @return Redirección a la vista de administración de secciones.
     */
    @PostMapping("/new")
    public String createSection(@ModelAttribute("guideSection") GuideSection section) {
        guideSectionService.saveSection(section);
        return "redirect:/admin/guide/manageguide";
    }

    /**
     * Muestra el formulario de edición para una sección existente.
     *
     * @param id ID de la sección a editar.
     * @param model Modelo para pasar datos a la vista.
     * @return Vista del formulario de edición de sección.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        GuideSection section = guideSectionService.getSectionById(id);
        model.addAttribute("guideSection", section);
        return "admin/guideSectionForm";
    }

    /**
     * Procesa el formulario de edición de una sección de la guía.
     *
     * @param id ID de la sección a editar.
     * @param section Objeto GuideSection actualizado.
     * @return Redirección a la vista de administración de secciones.
     */
    @PostMapping("/edit/{id}")
    public String updateSection(@PathVariable Long id, @ModelAttribute("guideSection") GuideSection section) {
        section.setId(id); // Asegura que el ID es correcto
        guideSectionService.saveSection(section);
        return "redirect:/admin/guide/manageguide";
    }
 
    /**
     * Muestra una página de confirmación antes de eliminar una sección de la guía.
     *
     * @param id ID de la sección a eliminar.
     * @param model Modelo para pasar datos a la vista.
     * @return Vista de confirmación de eliminación.
     */
    @GetMapping("/confirm-delete/{id}")
    public String confirmDelete(@PathVariable Long id, Model model) {
        GuideSection section = guideSectionService.getSectionById(id);
        model.addAttribute("message", "¿Seguro que quieres eliminar la sección: '" + section.getTitle() + "'?");
        model.addAttribute("action", "/admin/guide/delete/" + id);
        model.addAttribute("cancelUrl", "/admin/guide/manageguide");
        return "admin/confirm-delete";
    }

    /**
     * Elimina una sección de la guía por su ID y redirige a la administración de secciones.
     *
     * @param id ID de la sección a eliminar.
     * @param redirectAttributes Atributos para mensajes flash en la redirección.
     * @return Redirección a la vista de administración de secciones con mensaje de éxito.
     */
    @PostMapping("/delete/{id}")
    public String deleteSection(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        guideSectionService.deleteSectionById(id);
        redirectAttributes.addFlashAttribute("success", "Sección eliminada correctamente.");
        return "redirect:/admin/guide/manageguide";
    }

}