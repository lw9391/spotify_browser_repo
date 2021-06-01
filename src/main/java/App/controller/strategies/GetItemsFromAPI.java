package App.controller.strategies;

import App.controller.requests.Request;
import App.model.comm.spotify_objects.SpotifyObject;
import App.model.response.ModelResponse;
import App.view.ViewController;

import java.util.List;
import java.util.Optional;

public class GetItemsFromAPI implements AppStrategy {
    private final ViewController viewController;
    private final Request request;

    public GetItemsFromAPI(ViewController viewController, Request request) {
        this.viewController = viewController;
        this.request = request;
    }

    @Override
    public void execute() {
        ModelResponse response = request.execute();
        Optional<List<? extends SpotifyObject>> optional = response.getRequestedObjectsList();
        if (optional.isPresent()) {
            viewController.setCurrentlyViewedRequest(optional.get());
        } else {
            viewController.printStatusInfo(response.getStatus());
        }
    }
}
