package App.model.response;

import App.model.comm.spotify_objects.SpotifyObject;

import java.util.List;
import java.util.Optional;

public class BasicResponse implements ModelResponse {
    private final ResponseStatus status;
    private final String message;
    private final List<? extends SpotifyObject> requestedObjects;

    public BasicResponse(ResponseStatus status) {
        this.status = status;
        this.message = "";
        this.requestedObjects = null;
    }

    public BasicResponse(ResponseStatus status, String message) {
        this.status = status;
        this.message = message;
        this.requestedObjects = null;
    }

    public BasicResponse(List<? extends SpotifyObject> requestedObjects) {
        this.status = ResponseStatus.OBJECTS_RECEIVED;
        this.message = "";
        this.requestedObjects = requestedObjects;
    }

    @Override
    public ResponseStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Optional<List<? extends SpotifyObject>> getRequestedObjectsList() {
        return Optional.ofNullable(requestedObjects);
    }
}
