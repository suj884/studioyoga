package studioyoga.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import studioyoga.project.model.Classes;

/**
 * DTO (Data Transfer Object) para representar una clase junto con el número de plazas disponibles.
 * Se utiliza para transferir información de clases y plazas restantes entre capas de la aplicación.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClassesDTO {
    /**
     * Objeto que representa la clase.
     */
    private Classes classes;

    /**
     * Número de plazas disponibles para la clase.
     */
    private int spotsLeft;
}