package studioyoga.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tickets") // Nombre de la tabla en la base de datos
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String asunto;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private String estado; // Por ejemplo: "Abierto", "En progreso", "Cerrado"

    @ManyToOne
    private User usuario; // Usuario que reporta la incidencia

    // getters y setters
}
