package App.model;

import App.model.comm.CommunicationException;
import App.model.comm.CommunicationService;
import App.model.comm.spotify_objects.SpotifyObject;
import App.model.comm.spotify_objects.category;
import App.model.comm.spotify_objects.playlists;
import App.model.response.BasicResponse;
import App.model.response.ModelResponse;
import App.model.response.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

public final class AuthorizedManagerDecorator implements Manager {
    private final CommunicationService communicationService;
    private List<category> tempCategoryList;

    public AuthorizedManagerDecorator(Manager manager) {
        this.communicationService = new CommunicationService();
        BasicManager basicManager1 = (BasicManager) manager;
        this.communicationService.setUser(basicManager1.getUser());
        this.tempCategoryList = new ArrayList<>();
    }

    @Override
    public ModelResponse accessNewReleases() {
        try {
            List<? extends SpotifyObject> responseList = communicationService.getNewReleases();
            return new BasicResponse(responseList);
        } catch (CommunicationException e) {
            return new BasicResponse(ResponseStatus.CONNECTION_PROBLEM, e.getMessage());
        }
    }

    @Override
    public ModelResponse accessFeatured() {
        try {
            List<playlists> responseList = communicationService.getFeatured();
            return new BasicResponse(responseList);
        } catch (CommunicationException e) {
            return new BasicResponse(ResponseStatus.CONNECTION_PROBLEM, e.getMessage());
        }
    }

    @Override
    public ModelResponse accessCategories() {
        try {
            List<category> responseList = communicationService.getCategories();
            tempCategoryList = responseList;
            return new BasicResponse(responseList);
        } catch (CommunicationException e) {
            return new BasicResponse(ResponseStatus.CONNECTION_PROBLEM, e.getMessage());
        }
    }

    @Override
    public ModelResponse accessConcretePlaylist(String playlistName) {
        if (tempCategoryList.isEmpty()) {
            return new BasicResponse(ResponseStatus.CATEGORY_LIST_REQUIRED, "First access categories list use \"categories\" command.");
        }
        try {
            List<playlists> requestedPlaylist = new ArrayList<>();
            for (category ct : tempCategoryList) {
                if (ct.getName().equals(playlistName)) {
                    requestedPlaylist = communicationService.getConcretePlaylist(ct.getId());
                }
            }
            if (requestedPlaylist.isEmpty()) {
                return new BasicResponse(ResponseStatus.UNKNOWN_CATEGORY_REQUESTED);
            } else {
                return new BasicResponse(requestedPlaylist);
            }
        } catch (CommunicationException e) {
            return new BasicResponse(ResponseStatus.RECEIVED_ERROR_API_RESPONSE, e.getMessage());
        }
    }

    @Override
    public ModelResponse accessAuthorizationCode() {
        return new BasicResponse(ResponseStatus.AUTHORIZATION_ALREADY_GRANTED);
    }

    @Override
    public ModelResponse accessTokens() {
        return new BasicResponse(ResponseStatus.AUTHORIZATION_ALREADY_GRANTED);
    }


}
