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
    public String editPostForm(@PathVariable Integer id, Model model) {
        Optional<BlogPost> postOpt = blogService.findById(id);
        if (postOpt.isPresent()) {
            model.addAttribute("post", postOpt.get());
            return "admin/blogForm";
        } else {
            return "redirect:/blog/manage";
        }
    }

    // Guardar nuevo post o actualizar existente
    @PostMapping("/save")
    public String savePost(@ModelAttribute BlogPost post) {
        blogService.save(post);
        return "redirect:/blog/manage";
    }

    // Eliminar post
    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable Integer id) {
        blogService.deleteById(id);
        return "redirect:/blog/manage";
    }
}