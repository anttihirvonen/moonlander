package rocketpower.library;


import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;


interface TrackContainerListener {
    public void trackAdded(String name);
    public void trackDeleted(String name);
}

class TrackContainer {
    private HashMap<String, Track> tracks;
    private ArrayList<TrackContainerListener> listeners;

    /**
     * Initializes new empty TrackContainer.
     */
    public TrackContainer() {
        tracks = new HashMap<String, Track>();
        listeners = new ArrayList<TrackContainerListener>();
    }

    public void addEventListener(TrackContainerListener listener) {
        listeners.add(listener);
    }

    public void removeEventListener(TrackContainerListener listener) {
        listeners.remove(listener);
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
    public void deleteTrack(String name) {
        if (tracks.get(name) != null) {
            tracks.remove(name);
            for (TrackContainerListener l: listeners)
                l.trackDeleted(name);
        }
    }

    /**
     * Returns unmodifiable list of Track-objects.
     */
    public List<Track> getTracks() {
        return Collections.unmodifiableList(new ArrayList<Track>(tracks.values()));
    }
}
