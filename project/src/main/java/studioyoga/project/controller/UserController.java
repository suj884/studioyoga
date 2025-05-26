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
import org.springframework.web.bind.annotation.*;
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

    private final UserService userService;
    private final RolService rolService;
    private final UserRepository userRepository;
    private final RolRepository rolRepository;

    @Autowired
    public UserController(UserService userService,
            RolService rolService,
            UserRepository userRepository,
            RolRepository rolRepository) {
        this.userService = userService;
        this.rolService = rolService;
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
    }

    // ============ MÉTODOS PRINCIPALES ============

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

        // Mensaje si no hay resultados
        boolean searchPerformed = (name != null && !name.isEmpty()) || (role != null && !role.isEmpty());
        if (searchPerformed && users.isEmpty()) {
            model.addAttribute("info", "No existen usuarios con ese criterio");
        }

        return "admin/manageuser";
    }

    // ============ CREACIÓN/EDICIÓN ============

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

        // Lógica de subida de imagen
        if (!file.isEmpty()) {
            String uploadDir = "src/main/resources/static/images/profile-pictures/";
            String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Files.copy(file.getInputStream(),
                    Paths.get(uploadDir + fileName),
                    StandardCopyOption.REPLACE_EXISTING);
            user.setProfilePicture("/images/profile-pictures/" + fileName);
        }

        // Mantener contraseña si es edición
        if (user.getId() != null && user.getPassword().isEmpty()) {
            User existingUser = userRepository.findById(user.getId()).orElseThrow();
            user.setPassword(existingUser.getPassword());
        }

        userService.save(user);
        redirectAttributes.addFlashAttribute("success",
                user.getId() == null ? "Usuario creado correctamente" : "Usuario actualizado correctamente");

        return "redirect:/admin/users/manageuser";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        User user = userService.findById(id);
        if (user == null) {
            model.addAttribute("error", "Usuario no encontrado");
            return "redirect:/admin/users/manageuser";
        }

        model.addAttribute("user", user);
        model.addAttribute("roles", rolRepository.findAll());
        return "admin/formCreateEditUsers";
    }

    // ============ BORRADO ============

    @GetMapping("/confirm-delete/{id}")
    public String confirmDelete(@PathVariable Integer id, Model model) {
        User user = userService.findById(id);
        if (user == null) {
            model.addAttribute("error", "Usuario no encontrado");
            return "redirect:/admin/users/manageuser";
        }

        model.addAttribute("message",
                "¿Seguro que quieres eliminar al usuario: " +
                        user.getFirstLastName() + " " + user.getSecondLastName() + "?");
        model.addAttribute("action", "/admin/users/delete/" + id);
        model.addAttribute("cancelUrl", "/admin/users/manageuser");

        return "admin/confirm-delete";
    }

@PostMapping("/delete/{id}")
public String deleteUser(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
    try {
        userService.deleteUserAndReservations(id);
        redirectAttributes.addFlashAttribute("success", "Usuario eliminado correctamente");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Error al eliminar el usuario: " + e.getMessage());
    }
    return "redirect:/admin/users/manageuser";
}


    // ============ CAMBIO DE CONTRASEÑA ============

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String newPassword,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        userService.updatePassword(principal.getName(), newPassword);
        redirectAttributes.addFlashAttribute("success", "Contraseña actualizada correctamente");
        return "redirect:/profile";
    }
}
