package App.controller.strategies;

import App.controller.requests.RequestType;

public interface StrategyFactory {
    AppStrategy newInstance(RequestType type);
}
