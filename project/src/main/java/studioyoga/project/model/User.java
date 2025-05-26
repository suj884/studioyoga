package studioyoga.project.model;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
 
import jakarta.persistence.*;
import lombok.*;
 
/**
 * Entidad que representa un usuario del sistema.
 * Implementa {@link UserDetails} para la integración con Spring Security.
 * Contiene información personal, credenciales, rol, estado y otros datos relevantes del usuario.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {
    /**
     * Identificador único del usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
 
    /**
     * Nombre del usuario.
     */
    @Column(length = 50, nullable = false)
    private String name;
 
    /**
     * Primer apellido del usuario.
     */
    @Column(length = 50, nullable = false)
    private String firstLastName;
 
    /**
     * Segundo apellido del usuario (opcional).
     */
    @Column(length = 50)
    private String secondLastName;
 
    /**
     * Correo electrónico del usuario (único).
     */
    @Column(length = 100, nullable = false, unique = true)
    private String email;
 
    /**
     * Contraseña cifrada del usuario.
     */
    @Column(length = 255, nullable = false)
    private String password;

    /**
     * Fecha de registro del usuario.
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate registrationDate;

    /**
     * URL o ruta de la imagen de perfil del usuario.
     */
    private String profilePicture;

    /**
     * Número de teléfono del usuario.
     */
    private String phoneNumber;

    /**
     * Indica si el usuario está habilitado.
     */
    private boolean enabled;

    /**
     * Código de verificación para la activación del usuario.
     */
    private String verificationCode;
 
    /**
     * Rol asignado al usuario.
     */
    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;
 
    /**
     * Devuelve la colección de autoridades (roles) del usuario.
     *
     * @return Colección con el rol del usuario.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(rol); // rol ya implementa GrantedAuthority
    }  
 
    /**
     * Devuelve el nombre de usuario utilizado para la autenticación (correo electrónico).
     *
     * @return Correo electrónico del usuario.
     */
    @Override
    public String getUsername() {
        return email; // Devuelve el correo electrónico como nombre de usuario
    }

    /**
     * Notas adicionales sobre el usuario.
     */
    @Column(length = 1000)
    private String notes;
   
}