package studioyoga.project.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Objects;
import org.springframework.security.core.GrantedAuthority;

/**
 * Entidad que representa un rol de usuario en el sistema.
 * Implementa {@link GrantedAuthority} para la integración con Spring Security.
 * Cada rol tiene un nombre único y puede ser asignado a uno o varios usuarios.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Rol implements GrantedAuthority {
    /**
     * Identificador único del rol.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nombre del rol (debe ser único y no nulo).
     */
    @Column(length = 50, nullable = false, unique = true)
    private String name;

    /**
     * Devuelve la autoridad del rol con el prefijo "ROLE_" requerido por Spring Security.
     *
     * @return Nombre del rol con el prefijo "ROLE_".
     */
    @Override
    public String getAuthority() {
        return "ROLE_" + name; // Prefijo que Spring Security espera
    }

    /**
     * Calcula el hashCode basado en el id y el nombre del rol.
     *
     * @return Valor hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    /**
     * Compara este rol con otro objeto para determinar igualdad.
     *
     * @param obj Objeto a comparar.
     * @return true si ambos objetos son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Rol other = (Rol) obj;
        return Objects.equals(id, other.id) && Objects.equals(name, other.name);
    }
}