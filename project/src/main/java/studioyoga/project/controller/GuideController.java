package studioyoga.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import studioyoga.project.model.GuideSection;
import studioyoga.project.service.GuideSectionService;

@Controller
@RequestMapping("/admin/guide")
public class GuideController {
		    // Inyectar el servicio de secciones de guía
    private final GuideSectionService guideSectionService;

    public GuideController(GuideSectionService guideSectionService) {
        this.guideSectionService = guideSectionService;
    }
    
    @GetMapping("manageguide")
    public String listGuideSections(Model model) {
        model.addAttribute("sections", guideSectionService.getAllSections());
        return "admin/manageguide";
    }

 // Mostrar formulario de creación
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("guideSection", new GuideSection());
        return "admin/guideSectionForm";
    }

    // Procesar formulario de creación
    @PostMapping("/new")
    public String createSection(@ModelAttribute("guideSection") GuideSection section) {
        guideSectionService.saveSection(section);
        return "redirect:/admin/guide/manageguide";

    }
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        GuideSection section = guideSectionService.getSectionById(id);
        model.addAttribute("guideSection", section);
        return "admin/guideSectionForm";
    }
    @PostMapping("/edit/{id}")
    public String updateSection(@PathVariable Long id, @ModelAttribute("guideSection") GuideSection section) {
        section.setId(id); // Asegura que el ID es correcto
        guideSectionService.saveSection(section);
        return "redirect:/admin/guide/manageguide"; // O la ruta de tu listado
    }



    // Eliminar sección
    @PostMapping("/delete/{id}")
    public String deleteSection(@PathVariable Long id) {
        guideSectionService.deleteSectionById(id);
        return "redirect:/admin/guide/manageguide";

    }

}

   
