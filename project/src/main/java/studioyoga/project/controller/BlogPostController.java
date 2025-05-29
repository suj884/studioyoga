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

import studioyoga.project.constants.RedirConstants;
import studioyoga.project.model.BlogPost;
import studioyoga.project.service.BlogService;

/**
 * Controlador para la gestión de publicaciones del blog en el panel de administración.
 * Permite listar, crear, editar, guardar y eliminar entradas del blog.
 */
@Controller
@RequestMapping("/admin/blog")
public class BlogPostController {

    @Autowired
    private BlogService blogService;

    /**
     * Muestra la lista de publicaciones del blog para administración.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return Vista de administración de publicaciones del blog.
     */
    @GetMapping("/manageblog")
    public String manageBlog(Model model) {
        List<BlogPost> posts = blogService.findAll();
        model.addAttribute("blogPosts", posts);
        return "admin/manageblog";
    }

    /**
     * Muestra el formulario para crear una nueva publicación del blog.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return Vista del formulario de creación de publicación.
     */
    @GetMapping("/new")
    public String newPostForm(Model model) {
        BlogPost post = new BlogPost();
        post.setPublishedDate(LocalDate.now());
        model.addAttribute("post", post);
        return "admin/blogForm";
    }

    /**
     * Muestra el formulario para editar una publicación existente.
     *
     * @param id ID de la publicación a editar.
     * @param model Modelo para pasar datos a la vista.
     * @param redirectAttributes Atributos para mensajes flash en la redirección.
     * @return Vista del formulario de edición o redirección si no se encuentra la publicación.
     */
    @GetMapping("/edit/{id}")
    public String editPostForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<BlogPost> postOpt = blogService.findById(id);
        if (postOpt.isPresent()) {
            model.addAttribute("post", postOpt.get());
            return "admin/blogForm";
        } else {
            redirectAttributes.addFlashAttribute("error", "No se ha encontrado el post solicitado.");
            return RedirConstants.REDIRECT_ADMIN_BLOG;
        }
    }

    /**
     * Guarda una nueva publicación o actualiza una existente.
     *
     * @param post Objeto BlogPost a guardar.
     * @param redirectAttributes Atributos para mensajes flash en la redirección.
     * @return Redirección a la vista de administración de publicaciones.
     */
    @PostMapping("/save")
    public String savePost(@ModelAttribute BlogPost post, RedirectAttributes redirectAttributes) {
        blogService.save(post);
        redirectAttributes.addFlashAttribute("success", "Entrada de blog guardada correctamente.");
        return RedirConstants.REDIRECT_ADMIN_BLOG;
    }
    
    /**
     * Muestra una página de confirmación antes de eliminar una publicación.
     *
     * @param id ID de la publicación a eliminar.
     * @param model Modelo para pasar datos a la vista.
     * @return Vista de confirmación de eliminación o redirección si no se encuentra la publicación.
     */
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
        return "admin/confirm-delete";
    }
    
    /**
     * Elimina una publicación del blog.
     *
     * @param id ID de la publicación a eliminar.
     * @param redirectAttributes Atributos para mensajes flash en la redirección.
     * @return Redirección a la vista de administración de publicaciones.
     */
    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        blogService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Entrada de blog eliminada correctamente.");
        return "redirect:/admin/blog/manageblog";
    }
}