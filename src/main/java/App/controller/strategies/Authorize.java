package App.controller.strategies;

import App.controller.SpotifyBrowserApp;
import App.controller.requests.Request;
import App.controller.requests.ProvideAccessToken;
import App.controller.requests.ProvideAuthorizationCode;
import App.model.Manager;
import App.model.auth.AuthorizationService;
import App.model.response.ModelResponse;
import App.model.response.ResponseStatus;
import App.view.ViewController;

public final class Authorize implements AppStrategy {
    private final ViewController view;
    private final SpotifyBrowserApp app;
    private final Request accessAuthCode;
    private final Request accessAuthTokens;

    public Authorize(Manager manager, ViewController view, SpotifyBrowserApp app) {
        this.view = view;
        this.app = app;
        this.accessAuthCode = new ProvideAuthorizationCode(manager);
        this.accessAuthTokens = new ProvideAccessToken(manager);
    }

    @Override
    public void execute() {
        if (getAuthorizationCode()) {
            getAccessTokens();
        }
    }

    private boolean getAuthorizationCode() {
        String authLink = AuthorizationService.SCHEME + "://" + AuthorizationService.ACCESS_SERVER_POINT + "/authorize?" + AuthorizationService.QUERY;
        view.sendMessage("use this link to request the access code:" + "\n" +
                authLink + "\n" +
                "\n" + "waiting for code...");

        ModelResponse responseForCodeRequest = accessAuthCode.execute();
        view.printResponseStatusAndMessage(responseForCodeRequest);
        return responseForCodeRequest.getStatus().equals(ResponseStatus.AUTHORIZATION_CODE_RECEIVED);
    }

    private void getAccessTokens() {
        view.sendMessage("Making http request for access token...");
        ModelResponse responseForTokens = accessAuthTokens.execute();
        if (responseForTokens.getStatus().equals(ResponseStatus.AUTHORIZATION_GRANTED)) {
            app.extendAccess();
        }
        view.printStatusInfo(responseForTokens.getStatus());
    }
}
