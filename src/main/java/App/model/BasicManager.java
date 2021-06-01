package App.model;

import App.model.auth.AuthorizationException;
import App.model.auth.AuthorizationService;
import App.model.comm.User;
import App.model.response.BasicResponse;
import App.model.response.ModelResponse;
import App.model.response.ResponseStatus;

public final class BasicManager implements Manager {
    private final AuthorizationService authorizationService;
    private User user;

    public BasicManager() {
        authorizationService = new AuthorizationService();
    }

    @Override
    public ModelResponse accessNewReleases() {
        return new BasicResponse(ResponseStatus.AUTHORIZATION_REQUIRED);
    }

    @Override
    public ModelResponse accessFeatured() {
        return new BasicResponse(ResponseStatus.AUTHORIZATION_REQUIRED);
    }

    @Override
    public ModelResponse accessCategories() {
        return new BasicResponse(ResponseStatus.AUTHORIZATION_REQUIRED);
    }

    @Override
    public ModelResponse accessConcretePlaylist(String playlistName) {
        return new BasicResponse(ResponseStatus.AUTHORIZATION_REQUIRED);
    }

    @Override
    public ModelResponse accessAuthorizationCode() {
        boolean isCodeReceived;
        try {
            isCodeReceived = authorizationService.startServerAndWaitForCode();
            if (isCodeReceived) {
                return new BasicResponse(ResponseStatus.AUTHORIZATION_CODE_RECEIVED);
            }
        } catch (AuthorizationException e) {
            return new BasicResponse(ResponseStatus.AUTHORIZATION_CODE_NOT_FOUND, e.getMessage());
        }
        return new BasicResponse(ResponseStatus.AUTHORIZATION_CODE_NOT_FOUND);
    }

    @Override
    public ModelResponse accessTokens() {
        try {
            this.user = authorizationService.requestTokensAndCreateUser();
        } catch (AuthorizationException e) {
            return new BasicResponse(ResponseStatus.AUTHORIZATION_TOKENS_NOT_FOUND, e.getMessage());
        }
        return new BasicResponse(ResponseStatus.AUTHORIZATION_GRANTED);
    }

    protected User getUser() {
        return user;
    }
}