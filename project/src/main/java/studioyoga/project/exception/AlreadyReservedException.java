package studioyoga.project.exception;

public class AlreadyReservedException extends RuntimeException {
    public AlreadyReservedException() {
        super("Ya tienes una reserva para esta clase.");
    }
     public AlreadyReservedException(String message) {
        super(message);
    }
}