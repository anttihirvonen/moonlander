package rocketpower.library;


import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;


public class TrackContainer {
    private HashMap<String, Track> tracks;

    /**
     * Initializes new empty TrackContainer.
     */
    public TrackContainer() {
        tracks = new HashMap<String, Track>();
    }

    /**
     * Returns track with the given name or creates
     * a new track if it doesn't exist.
     *
     * @param name name of Track to get or create
     * @return Track object
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

    /**
     * Deletes a track.
     *
     * If track is not found, does nothing.
     *
     * @param name track to delete
     */
    public void deleteTrack(String name) {
        if (tracks.get(name) != null)
            tracks.remove(name);
    }

    /**
     * Returns unmodifiable list of Track-objects.
     */
    public List<Track> getTracks() {
        return Collections.unmodifiableList(new ArrayList<Track>(tracks.values()));
    }
}
