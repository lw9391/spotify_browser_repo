package App.model.response;


import App.model.comm.spotify_objects.SpotifyObject;

import java.util.List;
import java.util.Optional;

public interface ModelResponse {
    ResponseStatus getStatus();
    String getMessage();
    Optional<List<? extends SpotifyObject>> getRequestedObjectsList();
}
