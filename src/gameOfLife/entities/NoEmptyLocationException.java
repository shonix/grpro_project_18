package gameOfLife.entities;

public class NoEmptyLocationException extends RuntimeException {
    public NoEmptyLocationException(String message) {
        super(message);
    }
}
