package studioyoga.project.model;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
 
import jakarta.persistence.*;
import lombok.*;
 
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
 
    @Column(length = 50, nullable = false)
    private String name;
 
    @Column(length = 50, nullable = false)
    private String firstLastName;
 
    @Column(length = 50)
    private String secondLastName;
 
    @Column(length = 100, nullable = false, unique = true)
    private String email;
 
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
 
    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;
 
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(rol); // rol ya implementa GrantedAuthority
    }  
 
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