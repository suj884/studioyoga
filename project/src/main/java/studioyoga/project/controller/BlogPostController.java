package studioyoga.project.controller;

import java.time.LocalDate;
import java.util.Optional;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import studioyoga.project.model.BlogPost;
import studioyoga.project.service.BlogService;

@Controller
@RequestMapping("/admin/blog")
public class BlogPostController {

    @Autowired
    private BlogService blogService;

    // Mostrar lista de posts para administrar
    @GetMapping("/manageblog")
    public String manageBlog(Model model) {
        List<BlogPost> posts = blogService.findAll();
        model.addAttribute("blogPosts", posts);
        return "admin/manageblog";
    }

    // Mostrar formulario para crear nuevo post
@GetMapping("/new")
public String newPostForm(Model model) {
    BlogPost post = new BlogPost();
    post.setPublishedDate(LocalDate.now());
    model.addAttribute("post", post);
    return "admin/blogForm";
}
 // Mostrar formulario para editar post existente
    @GetMapping("/edit/{id}")
    public String editPostForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<BlogPost> postOpt = blogService.findById(id);
        if (postOpt.isPresent()) {
            model.addAttribute("post", postOpt.get());
            return "admin/blogForm";
        } else {
            redirectAttributes.addFlashAttribute("error", "No se ha encontrado el post solicitado.");
            return "redirect:/admin/blog/manageblog";
        }
    }

    // Guardar nuevo post o actualizar existente
    @PostMapping("/save")
    public String savePost(@ModelAttribute BlogPost post, RedirectAttributes redirectAttributes) {
        blogService.save(post);
        redirectAttributes.addFlashAttribute("success", "Entrada de blog guardada correctamente.");
        return "redirect:/admin/blog/manageblog";
    }

    // Eliminar post
    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        blogService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Entrada de blog eliminada correctamente.");
        return "redirect:/admin/blog/manageblog";
    }
@GetMapping("/confirm-delete/{id}")
public String confirmDelete(@PathVariable Integer id, Model model) {
    BlogPost post = blogService.findById(id).orElse(null);
    if (post == null) {
        model.addAttribute("error", "No se encontró el post.");
        return "redirect:/admin/blog/manageblog";
    }
    model.addAttribute("message", "¿Seguro que quieres eliminar el post: '" + post.getTitle() + "'?");
    model.addAttribute("action", "/admin/blog/delete/" + id);
    model.addAttribute("cancelUrl", "/admin/blog/manageblog");
    return "admin/confirm-delete"; // Vista que solo incluye el fragmento
}


}