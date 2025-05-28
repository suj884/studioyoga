package studioyoga.project.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import studioyoga.project.model.User;
import studioyoga.project.repository.TicketRepository;
import studioyoga.project.repository.UserRepository;

/**
 * Servicio para la gestión de usuarios.
 * Proporciona métodos para buscar, listar, crear, editar, eliminar usuarios,
 * actualizar contraseñas y filtrar usuarios por nombre y rol.
 */
@Service
public class UserService {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    /**
     * Constructor que inyecta el repositorio de usuarios.
     *
     * @param userRepository Repositorio de usuarios.
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Busca un usuario por su email.
     *
     * @param email Email del usuario.
     * @return Usuario encontrado.
     * @throws UsernameNotFoundException Si no se encuentra el usuario.
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
    }

    /**
     * Obtiene la lista de todos los usuarios registrados.
     *
     * @return Lista de usuarios.
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Busca un usuario por su ID.
     *
     * @param id ID del usuario.
     * @return Usuario encontrado.
     * @throws RuntimeException Si no se encuentra el usuario.
     */
    public User findById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    /**
     * Elimina un usuario por su ID.
     *
     * @param id ID del usuario a eliminar.
     */
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Guarda un nuevo usuario o actualiza uno existente.
     * Si la contraseña no está encriptada, la encripta antes de guardar.
     * Si es edición y no se cambia la contraseña, mantiene la anterior.
     *
     * @param user Usuario a guardar o actualizar.
     */
    public void save(User user) {
        if (user.getId() == null || (user.getPassword() != null && !user.getPassword().isEmpty())) {
            // Solo encripta si la contraseña NO está ya encriptada (BCrypt empieza por $2a$
            // o $2b$)
            String password = user.getPassword();
            if (password != null && !password.startsWith("$2a$") && !password.startsWith("$2b$")) {
                user.setPassword(passwordEncoder.encode(password));
            }
            // Si ya está encriptada, la dejamos tal cual
        } else {
            // Edición y no se cambió la contraseña
            User existingUser = userRepository.findById(user.getId()).orElseThrow();
            user.setPassword(existingUser.getPassword());
        }
        userRepository.save(user);
    }

    /**
     * Actualiza la contraseña de un usuario identificado por su email.
     *
     * @param email       Email del usuario.
     * @param newPassword Nueva contraseña a establecer.
     */
    public void updatePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email).orElseThrow();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * Busca usuarios filtrando por nombre/apellidos y/o rol.
     * Si ambos parámetros están vacíos, devuelve todos los usuarios.
     * Ordena los resultados por primer apellido, segundo apellido y nombre.
     *
     * @param name Nombre o apellido para filtrar (opcional).
     * @param role Rol para filtrar (opcional).
     * @return Lista de usuarios que cumplen los criterios.
     */
    public List<User> findUsersBySurnameAndNameAndRole(String name, String role) {
        final String finalRole = role;
        List<User> users;

        boolean hasName = (name != null && !name.trim().isEmpty());
        boolean hasRole = (role != null && !role.trim().isEmpty());

        if (hasName && hasRole) {
            // Buscar por nombre/apellidos y luego filtrar por rol
            users = userRepository
                    .findByFirstLastNameContainingIgnoreCaseOrSecondLastNameContainingIgnoreCaseOrNameContainingIgnoreCase(
                            name, name, name)
                    .stream()
                    .filter(u -> u.getRol() != null && finalRole.equalsIgnoreCase(u.getRol().getName()))
                    .collect(Collectors.toList());
        } else if (hasName) {
            users = userRepository
                    .findByFirstLastNameContainingIgnoreCaseOrSecondLastNameContainingIgnoreCaseOrNameContainingIgnoreCase(
                            name, name, name);
        } else if (hasRole) {
            users = userRepository.findByRol_Name(role);
        } else {
            users = userRepository.findAll();
        }

        // Ordenar por apellidos y nombre
        users.sort(Comparator
                .comparing(User::getFirstLastName, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(User::getSecondLastName, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(User::getName, String.CASE_INSENSITIVE_ORDER));

        return users;
    }

    /**
     * Elimina un usuario y todas sus reservas y tickets asociados (operación
     * transaccional).
     *
     * @param userId ID del usuario a eliminar.
     */

    @Transactional
    public void deleteUserAndReservations(Integer userId) {
        ticketRepository.deleteByUsuario_Id(userId); // Elimina tickets asociados
        reservationService.deleteByUserId(userId); // Elimina reservas asociadas
        userRepository.deleteById(userId); // Elimina el usuario
    }
}