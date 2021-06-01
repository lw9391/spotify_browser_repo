package App.model.comm;

public class CommunicationException extends Exception {
    public CommunicationException(String errorMessage) {
        super(errorMessage);
    }

    public CommunicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommunicationException(Throwable cause) {
        super(cause);
    }
}
