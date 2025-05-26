package studioyoga.project.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Entidad que representa un evento en el sistema.
 * Contiene información como el título, descripción, fecha, hora, imagen, ubicación y estado de actividad.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class Event {
    /**
     * Identificador único del evento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Título del evento.
     */
    private String title;

    /**
     * Descripción detallada del evento.
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Fecha en la que se realiza el evento.
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate eventDate;

    /**
     * Hora en la que se realiza el evento.
     */
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime eventTime;

    /**
     * URL de la imagen asociada al evento (opcional).
     */
    private String imageUrl;

    /**
     * Ubicación donde se realiza el evento (opcional).
     */
    private String location;

    /**
     * Indica si el evento está activo o ya ha pasado.
     */
    private boolean active = true;

    // Otros campos que necesites (precio, categoría, etc.)
}