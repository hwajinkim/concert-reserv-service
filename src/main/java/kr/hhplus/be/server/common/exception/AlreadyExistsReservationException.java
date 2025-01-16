package kr.hhplus.be.server.common.exception;

public class AlreadyExistsReservationException extends RuntimeException {
    public AlreadyExistsReservationException(String message) {
        super(message);
    }
}