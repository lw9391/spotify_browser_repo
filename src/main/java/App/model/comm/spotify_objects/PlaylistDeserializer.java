package App.model.comm.spotify_objects;

import com.google.gson.*;

import java.lang.reflect.Type;


public class PlaylistDeserializer implements JsonDeserializer<playlists> {
    @Override
    public playlists deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jo = json.getAsJsonObject();
        String name = jo.getAsJsonPrimitive("name").getAsString();
        String ext_url = jo.get("external_urls")
                .getAsJsonObject()
                .getAsJsonPrimitive("spotify")
                .getAsString();

        return new playlists(name, ext_url);
    }
}
