package App.model.comm.spotify_objects;

public abstract class SpotifyObject {
    private final String name;
    private final String ext_url;

    public SpotifyObject(String name, String ext_url) {
        this.name = name;
        this.ext_url = ext_url;
    }

    public String getName() {
        return name;
    }

    public String getExt_url() {
        return ext_url;
    }

    @Override
    public String toString() {
        return name + "\n" + ext_url + "\n";
    }
}
