package rocketpower.library;


import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;


public class TrackContainer {
    private HashMap<String, Track> tracks;

    public TrackContainer() {
        tracks = new HashMap<String, Track>();
    }

    /*
     * Get track with given name or create
     * new track if it doesn't exist
     */
    public Track getTrack(String name) {
        Track track = tracks.get(name);

        // Not in map, create new
        if (track == null) {
            track = new Track(name);
            tracks.put(name, track);
        }

        return track;
    }

    public void deleteTrack(String name) {
        if (tracks.get(name) != null)
            tracks.remove(name);
    }

    /*
     * Return unmodifiable list of Track-objects.
     */
    public List<Track> getTracks() {
        return Collections.unmodifiableList(new ArrayList<Track>(tracks.values()));
    }
}
