package Contract;

public class WrongMoveException extends RuntimeException {
    public WrongMoveException(String s) {
        super(s);
    }
    public WrongMoveException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
