package dat.exceptions;

public class DatabaseException extends RuntimeException {
    private int statusCode;

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
