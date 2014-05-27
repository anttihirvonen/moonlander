package moonlander.library;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;


interface TrackContainerListener {
    public void trackAdded(String name);
    public void trackDeleted(String name);
}

class TrackContainer {
    // LinkedHashMap keeps the order of added pairs,
    // so Track-objects can be also fetched by insertion id.
    private LinkedHashMap<String, Track> tracks;
    private ArrayList<TrackContainerListener> listeners;

    /**
     * Initializes new empty TrackContainer.
     */
    public TrackContainer() {
        tracks = new LinkedHashMap<String, Track>();
        listeners = new ArrayList<TrackContainerListener>();
    }

    public void addEventListener(TrackContainerListener listener) {
        listeners.add(listener);
    }

    public void removeEventListener(TrackContainerListener listener) {
        listeners.remove(listener);
    }

    public Track get(String name) {
        return tracks.get(name);
    }

    /**
     * Fetch track by it's zero-based insertion id.
     *
     * Tracks are in same order as they were added.
     * This directly maps to Rocket's insertion order and
     * the method is intended to be used when Rocket sends
     * SET_KEY, which contains id of the track.
     */
    public Track getById(int id) {
        return (new ArrayList<Track>(tracks.values())).get(id);
    }

    /**
     * Returns track with the given name or creates
     * a new track if it doesn't exist.
     *
     * @param name name of Track to get or create
     * @return Track object
     */
    public Track getOrCreate(String name) {
        Track track = get(name);

        // Not in map, create new
        if (track == null) {
            track = new Track(name);
            tracks.put(name, track);
            for (TrackContainerListener l: listeners)
                l.trackAdded(name);
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
    public void delete(String name) {
        if (tracks.get(name) != null) {
            tracks.remove(name);
            for (TrackContainerListener l: listeners)
                l.trackDeleted(name);
        }
    }

    /**
     * Returns unmodifiable list of Track-objects.
     */
    public List<Track> getAll() {
        return Collections.unmodifiableList(new ArrayList<Track>(tracks.values()));
    }
}
