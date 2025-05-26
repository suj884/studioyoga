package studioyoga.project.model;

/**
 * Representa una clase permitida en el sistema, indicando el día, la hora y el nombre de la clase.
 * Utilizado para validar horarios y tipos de clases disponibles.
 *
 * @param day Día de la semana en que se permite la clase.
 * @param time Hora en la que se permite la clase (formato HH:mm).
 * @param className Nombre de la clase permitida.
 */
public record AllowedClass (String day, String time, String className) {
    
    
}