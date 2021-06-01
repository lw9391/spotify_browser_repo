package App.model.comm.spotify_objects;

public class category extends SpotifyObject {
    private final String id;

    public category(String name, String id) {
        super(name, "category object doesnt support external urls");
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return super.getName() + "\n";
    }
}
