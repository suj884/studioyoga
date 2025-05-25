package studioyoga.project.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate eventDate;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime eventTime;
    private String imageUrl; // opcional, para la imagen del evento
    private String location; // opcional, para la ubicación
    private boolean active = true; // para marcar si el evento está activo o pasado
    // Otros campos que necesites (precio, categoría, etc.)
}
