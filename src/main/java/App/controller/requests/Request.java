package App.controller.requests;

import App.model.response.ModelResponse;

@FunctionalInterface
public interface Request {
    ModelResponse execute();
}