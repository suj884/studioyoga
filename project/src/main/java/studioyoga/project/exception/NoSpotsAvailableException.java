package studioyoga.project.exception;

/**
 * Excepción personalizada que se lanza cuando no hay plazas disponibles para una clase.
 */
public class NoSpotsAvailableException extends RuntimeException {
    /**
     * Crea una nueva excepción con el mensaje predeterminado.
     */
    public NoSpotsAvailableException() {
        super("No hay plazas disponibles para esta clase.");
    }

    /**
     * Crea una nueva excepción con un mensaje personalizado.
     *
     * @param message Mensaje descriptivo del error.
     */
    public NoSpotsAvailableException(String message) {
        super(message);
    }
}