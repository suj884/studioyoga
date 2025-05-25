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

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
 @Table(name = "reservations")
public class Reservation {

     @Id @GeneratedValue
    private Integer id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Classes classes;

    private LocalDateTime dateReservation;
    
    private boolean active = true;
}
