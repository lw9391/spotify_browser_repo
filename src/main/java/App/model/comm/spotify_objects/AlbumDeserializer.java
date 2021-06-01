package App.model.comm.spotify_objects;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AlbumDeserializer implements JsonDeserializer<albums> {
    @Override
    public albums deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jo = json.getAsJsonObject();
        String name = jo.getAsJsonPrimitive("name").getAsString();
        String ext_url = jo.get("external_urls")
                .getAsJsonObject()
                .getAsJsonPrimitive("spotify")
                .getAsString();
        List<String> artistsList = new ArrayList<>();
        JsonArray artists = jo.getAsJsonArray("artists");

        artists.forEach(artist -> {
            String artistName = artist.getAsJsonObject()
                    .get("name")
                    .getAsString();
            artistsList.add(artistName);
        });
        return new albums(name, ext_url, artistsList);
    }
}
