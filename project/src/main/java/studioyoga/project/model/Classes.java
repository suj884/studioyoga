package studioyoga.project.model;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa una clase de yoga.
 * Contiene información sobre el título, descripción, instructor, imagen, fecha, horario, ubicación,
 * estado de actividad, capacidad y otros detalles relevantes de la clase.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "classes")
public class Classes {
    /**
     * Identificador único de la clase.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Título de la clase.
     */
    private String title;

    /**
     * Descripción de la clase.
     */
    private String description;

    /**
     * Nombre del instructor de la clase (opcional).
     */
    private String instructor;

    /**
     * URL de la imagen asociada a la clase (opcional).
     */
    private String imageUrl;

    /**
     * Fecha en la que se imparte la clase.
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate eventDate;

    /**
     * Hora de inicio de la clase.
     */
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime timeInit;

    /**
     * Hora de finalización de la clase.
     */
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime timeEnd;

    /**
     * Ubicación donde se imparte la clase (opcional).
     */
    private String location;

    /**
     * Indica si la clase está activa o no.
     */
    private boolean active = true;

    /**
     * Capacidad máxima de alumnos para la clase.
     */
    private int capacity;
}