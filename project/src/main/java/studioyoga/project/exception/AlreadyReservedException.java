package studioyoga.project.exception;

/**
 * Excepción personalizada que se lanza cuando un usuario intenta reservar una clase
 * para la cual ya tiene una reserva existente.
 */
public class AlreadyReservedException extends RuntimeException {
    /**
     * Crea una nueva excepción con el mensaje predeterminado.
     */
    public AlreadyReservedException() {
        super("Ya tienes una reserva para esta clase.");
    }

    /**
     * Crea una nueva excepción con un mensaje personalizado.
     *
     * @param message Mensaje descriptivo del error.
     */
    public AlreadyReservedException(String message) {
        super(message);
    }
}