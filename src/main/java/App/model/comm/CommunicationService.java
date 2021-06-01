package App.model.comm;

import App.model.comm.spotify_objects.AlbumDeserializer;
import App.model.comm.spotify_objects.PlaylistDeserializer;
import App.model.comm.spotify_objects.SpotifyObject;
import App.model.comm.spotify_objects.albums;
import App.model.comm.spotify_objects.category;
import App.model.comm.spotify_objects.playlists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.ArrayList;
import java.util.List;

public final class CommunicationService {
    public static final String NEW_RELEASES_PATH = "/v1/browse/new-releases";
    public static final String FEATURED_CATEGORIES_PATH = "/v1/browse/featured-playlists";
    public static final String CATEGORIES_PATH = "/v1/browse/categories/";
    public static final String RESOURCE = "https://api.spotify.com";

    private User user;

    public List<albums> getNewReleases() throws CommunicationException {
        HttpResponse<String> response = sendBasicGetRequest(NEW_RELEASES_PATH, "");
        String responseJson = response.body();
        JsonObject pagingObject = JsonParser.parseString(responseJson)
                .getAsJsonObject()
                .getAsJsonObject("albums");
        return getSpotifyObjectList(pagingObject, albums.class, new AlbumDeserializer());
    }

    public List<playlists> getFeatured() throws CommunicationException {
        HttpResponse<String> response = sendBasicGetRequest(FEATURED_CATEGORIES_PATH, "");
        String responseJson = response.body();
        JsonObject pagingObject = JsonParser.parseString(responseJson)
                .getAsJsonObject()
                .getAsJsonObject("playlists");
        return getSpotifyObjectList(pagingObject, playlists.class, new PlaylistDeserializer());
    }

    public List<category> getCategories () throws CommunicationException {
        HttpResponse<String> response = sendBasicGetRequest(CATEGORIES_PATH, "");
        String responseJson = response.body();
        JsonObject pagingObject = JsonParser.parseString(responseJson)
                .getAsJsonObject()
                .getAsJsonObject("categories");
        return getSpotifyObjectList(pagingObject, category.class);
    }

    public List<playlists> getConcretePlaylist(String playlistId) throws CommunicationException {
        HttpResponse<String> response = sendBasicGetRequest(CATEGORIES_PATH + playlistId + "/playlists", "");
        String responseJson = response.body();
        if (responseJson.contains("error")) {
            String errorMessage = getErrorMessage(responseJson);
            throw new CommunicationException(errorMessage);
        } else {
            JsonObject pagingObject = JsonParser.parseString(responseJson)
                    .getAsJsonObject()
                    .getAsJsonObject("playlists");
            return getSpotifyObjectList(pagingObject, playlists.class, new PlaylistDeserializer());
        }
    }

        //basic GET request with "Authorization" header, use empty string if query isn't needed
    private HttpResponse<String> sendBasicGetRequest(String path, String query) throws CommunicationException {
        try {
            String endpoint = RESOURCE + path + "";
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .header("Authorization",user.getToken_type() + " " + user.getAccess_token())
                    .uri(URI.create(endpoint))
                    .GET()
                    .build();
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new CommunicationException("IO exception occurred during sending http request to api", e);
        } catch (InterruptedException ie) {
            throw new CommunicationException("Http request interrupted, try again", ie);
        }
    }

    //this helper method use custom deserialization to create Spotify Objects from paging object
    private <T extends SpotifyObject> List<T> getSpotifyObjectList(JsonObject pagingObject, Class<T> clazz, JsonDeserializer<T> deserializer) {
        Gson gson = new GsonBuilder().registerTypeAdapter(clazz, deserializer)
                .create();
        JsonArray items = pagingObject.getAsJsonArray("items");
        List<T> list = new ArrayList<>();
        for (JsonElement item : items) {
            list.add(gson.fromJson(item, clazz));
        }
        return list;
    }

    private <T extends SpotifyObject> List<T> getSpotifyObjectList(JsonObject pagingObject, Class<T> clazz) {
        Gson gson = new Gson();
        JsonArray items = pagingObject.getAsJsonArray("items");
        List<T> list = new ArrayList<>();
        for (JsonElement item : items) {
            list.add(gson.fromJson(item, clazz));
        }
        return list;
    }

    private String getErrorMessage(String responseJson) {
        return JsonParser.parseString(responseJson)
                .getAsJsonObject()
                .getAsJsonObject("error")
                .get("message")
                .getAsString();
    }

    public void setUser(User user) {
        this.user = user;
    }
}



