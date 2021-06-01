package App.model.comm.spotify_objects;

import java.util.List;

//simplified album object from spotify api
public class albums extends SpotifyObject {
    private final List<String> artistsList;

    public albums(String name, String externalURL, List<String> artistsList) {
        super(name, externalURL);
        this.artistsList = artistsList;
    }

    public void addArtist(String name) {
        artistsList.add(name);
    }

    @Override
    public String toString() {
            return getName() + "\n" + artistsList + "\n" + getExt_url() + "\n";
        }
}
