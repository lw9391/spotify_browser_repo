package App.controller.requests;

import App.model.Manager;
import App.model.response.ModelResponse;

public class GetConcretePlaylist implements Request {
    private final String categoryName;
    private final Manager manager;

    public GetConcretePlaylist(String additionalParams, Manager manager) {
        this.categoryName = additionalParams;
        this.manager = manager;
    }

    @Override
    public ModelResponse execute() {
        return manager.accessConcretePlaylist(categoryName);
    }
}