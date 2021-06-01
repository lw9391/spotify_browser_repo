package App.controller.requests;


import App.model.Manager;
import App.model.response.ModelResponse;

public class ProvideAccessToken implements Request {
    private final Manager manager;

    public ProvideAccessToken(Manager manager) {
        this.manager = manager;
    }

    @Override
    public ModelResponse execute() {
        return manager.accessTokens();
    }
}
