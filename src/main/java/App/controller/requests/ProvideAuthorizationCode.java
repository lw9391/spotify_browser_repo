package App.controller.requests;


import App.model.Manager;
import App.model.response.ModelResponse;

public class ProvideAuthorizationCode implements Request {
    private final Manager manager;

    public ProvideAuthorizationCode(Manager manager) {
        this.manager = manager;
    }

    @Override
    public ModelResponse execute() {
        return manager.accessAuthorizationCode();
    }
}
