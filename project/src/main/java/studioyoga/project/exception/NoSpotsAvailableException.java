package studioyoga.project.exception;

public class NoSpotsAvailableException extends RuntimeException {
    public NoSpotsAvailableException() {
        super("No hay plazas disponibles para esta clase.");
    }
    public NoSpotsAvailableException(String message) {
        super(message);
    }
}

