package studioyoga.project.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio personalizado para la autenticación de usuarios con Spring Security.
 * Implementa {@link UserDetailsService} y utiliza el {@link UserService} para cargar usuarios por email.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    /**
     * Constructor que inyecta el servicio de usuarios.
     *
     * @param userService Servicio de usuarios.
     */
    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Carga los detalles de un usuario por su nombre de usuario (email).
     *
     * @param username Email del usuario.
     * @return Detalles del usuario para autenticación.
     * @throws UsernameNotFoundException Si no se encuentra el usuario.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.findByEmail(username);
    }
}