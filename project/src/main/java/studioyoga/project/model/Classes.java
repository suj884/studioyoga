package studioyoga.project.model;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "classes")
public class Classes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;
    private String instructor; // opcional, para la ubicación
    private String imageUrl; // opcional, para la imagen del evento
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate eventDate;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime timeInit;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime timeEnd;
    private String location; // opcional, para la ubicación
    private boolean active = true; // para marcar si el evento está activo o pasado
    private int capacity;
   




}
