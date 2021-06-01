package App.model;


import App.model.response.ModelResponse;

public interface Manager {

    ModelResponse accessNewReleases();

    ModelResponse accessFeatured();

    ModelResponse accessCategories();

    ModelResponse accessConcretePlaylist(String playlistName);

    ModelResponse accessAuthorizationCode();

    ModelResponse accessTokens();
}
