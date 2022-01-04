package domain;

public class IllegalMoveException extends Exception {
    public IllegalMoveException() {

    };
    public IllegalMoveException(String errorMessage) {
        super(errorMessage);
    }
}