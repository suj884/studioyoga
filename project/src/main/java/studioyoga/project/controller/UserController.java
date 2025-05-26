package studioyoga.project.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import studioyoga.project.model.Rol;
import studioyoga.project.model.User;
import studioyoga.project.repository.RolRepository;
import studioyoga.project.repository.UserRepository;
import studioyoga.project.service.RolService;
import studioyoga.project.service.UserService;

/**
 * Controlador para la gestión de usuarios en el panel de administración.
 * Permite listar, buscar, crear, editar, eliminar usuarios, así como cambiar contraseñas y gestionar imágenes de perfil.
 */
@Controller
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;
    private final RolService rolService;
    private final UserRepository userRepository;
    private final RolRepository rolRepository;

    /**
     * Constructor que inyecta los servicios y repositorios necesarios.
     *
     * @param userService Servicio de usuarios.
     * @param rolService Servicio de roles.
     * @param userRepository Repositorio de usuarios.
     * @param rolRepository Repositorio de roles.
     */
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

    /**
     * Muestra la lista de usuarios, permite filtrar por nombre/apellido y rol.
     *
     * @param name Nombre o apellido para filtrar (opcional).
     * @param role Rol para filtrar (opcional).
     * @param model Modelo para pasar datos a la vista.
     * @return Vista de administración de usuarios.
     */
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

    /**
     * Muestra el formulario para crear un nuevo usuario.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return Vista del formulario de creación/edición de usuario.
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", rolRepository.findAll());
        return "admin/formCreateEditUsers";
    }

    /**
     * Guarda un nuevo usuario o actualiza uno existente, gestionando la imagen de perfil.
     *
     * @param user Objeto User a guardar.
     * @param file Archivo de imagen de perfil.
     * @param redirectAttributes Atributos para mensajes flash en la redirección.
     * @return Redirección a la vista de administración de usuarios.
     * @throws IOException Si ocurre un error al guardar la imagen.
     */
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

    /**
     * Muestra el formulario de edición para un usuario existente.
     *
     * @param id ID del usuario a editar.
     * @param model Modelo para pasar datos a la vista.
     * @return Vista del formulario de edición de usuario o redirección si no se encuentra.
     */
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

    /**
     * Muestra una página de confirmación antes de eliminar un usuario.
     *
     * @param id ID del usuario a eliminar.
     * @param model Modelo para pasar datos a la vista.
     * @return Vista de confirmación de eliminación o redirección si no se encuentra el usuario.
     */
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

    /**
     * Elimina un usuario y todas sus reservas asociadas.
     *
     * @param id ID del usuario a eliminar.
     * @param redirectAttributes Atributos para mensajes flash en la redirección.
     * @return Redirección a la vista de administración de usuarios.
     */
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

    /**
     * Cambia la contraseña del usuario autenticado.
     *
     * @param newPassword Nueva contraseña.
     * @param principal Usuario autenticado.
     * @param redirectAttributes Atributos para mensajes flash en la redirección.
     * @return Redirección al perfil del usuario.
     */
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String newPassword,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        userService.updatePassword(principal.getName(), newPassword);
        redirectAttributes.addFlashAttribute("success", "Contraseña actualizada correctamente");
        return "redirect:/profile";
    }
}