package studioyoga.project.model;
import jakarta.persistence.*;
import lombok.*;
import java.util.Objects;
import org.springframework.security.core.GrantedAuthority;

 
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Rol implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
 
    @Column(length = 50, nullable = false, unique = true)
    private String name;
 
    @Override
    public String getAuthority() {
        return "ROLE_" + name; // Prefijo que Spring Security espera
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Rol other = (Rol) obj;
        return Objects.equals(id, other.id) && Objects.equals(name, other.name);
    }
 
}