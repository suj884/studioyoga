package studioyoga.project.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import studioyoga.project.model.Rol;
import studioyoga.project.model.User;
import studioyoga.project.repository.RolRepository;
import studioyoga.project.repository.UserRepository;
import studioyoga.project.service.UserService;

@Controller
@RequestMapping("/admin/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RolRepository rolRepository;
    private final RolService rolService;

    public UserController(UserService userService, RolService rolService) {
        this.userService = userService;
        this.rolService = rolService;
    }

    @GetMapping("/manageuser")
    public String listUsers(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String role,
        Model model) {

        List<User> users = userService.findUsersBySurnameAndNameAndRole(name, role);
        List<Rol> roles = rolService.findAll();

        model.addAttribute("users", users);
        model.addAttribute("name", name);
        model.addAttribute("role", role);
        model.addAttribute("roles", roles);

        boolean searchPerformed = (name != null && !name.isEmpty()) || (role != null && !role.isEmpty());
        if (searchPerformed && users.isEmpty()) {
            model.addAttribute("info", "No existen usuarios con ese criterio");
        }

        return "admin/manageuser";
    }



    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", rolRepository.findAll());
        return "admin/formCreateEditUsers";
    }

    @PostMapping("/save")
    public String saveUser(
            @ModelAttribute User user,
            @RequestParam("profilePictureFile") MultipartFile file,
            RedirectAttributes redirectAttributes) throws IOException {

        if (!file.isEmpty()) {
            String uploadDir = "src/main/resources/static/images/profile-pictures/";
            String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Files.copy(file.getInputStream(), Paths.get(uploadDir + fileName), StandardCopyOption.REPLACE_EXISTING);
            user.setProfilePicture("/images/profile-pictures/" + fileName);
        }
        if (user.getId() != null && (user.getPassword() == null || user.getPassword().isEmpty())) {
            User existingUser = userRepository.findById(user.getId()).orElseThrow();
            user.setPassword(existingUser.getPassword());
        }

        userService.save(user);
        redirectAttributes.addFlashAttribute("success", "Usuario guardado correctamente");
        return "redirect:/admin/users/manageUser";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("roles", rolRepository.findAll());
        return "admin/formCreateEditUsers";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Usuario eliminado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar el usuario.");
        }
        return "redirect:/admin/users/manageUser";
    }
    @PostMapping("/change-password")
public String changePassword(@RequestParam String newPassword, Principal principal, RedirectAttributes redirectAttributes) {
    userService.updatePassword(principal.getName(), newPassword);
    redirectAttributes.addFlashAttribute("success", "Contraseña actualizada correctamente");
    return "redirect:/profile";
}

}
