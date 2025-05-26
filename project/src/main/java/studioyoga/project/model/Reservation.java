package studioyoga.project.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa una reserva de clase realizada por un usuario.
 * Contiene información sobre el usuario, la clase reservada, la fecha de la reserva y el estado de la misma.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservations")
public class Reservation {

    /**
     * Identificador único de la reserva.
     */
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * Usuario que realiza la reserva.
     */
    @ManyToOne
    private User user;

    /**
     * Clase reservada por el usuario.
     */
    @ManyToOne
    private Classes classes;

    /**
     * Fecha y hora en que se realizó la reserva.
     */
    private LocalDateTime dateReservation;

    /**
     * Indica si la reserva está activa.
     */
    private boolean active = true;
}