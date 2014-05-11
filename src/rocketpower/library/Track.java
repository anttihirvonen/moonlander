package rocketpower.library;

import java.util.ArrayList;
import java.util.Collections;


public class Track {
    private String name;
    // ArrayList stored in sorted order by
    // key trackKey.row.
    // Not the most efficient data structure
    // for this use, but suffices for now.
    private ArrayList<TrackKey> keys;

    public Track(String name) {
        this.name = name;
        this.keys = new ArrayList<TrackKey>();
    }

    /*
     * Find key for the given row.
     *
     * @returns Key if found, otherwise null.
     */
    public TrackKey getKey(int row) {
        // TODO: better way to do this search
        int index = Collections.binarySearch(this.keys, new TrackKey(row, 0.f, TrackKey.KeyType.STEP));
        return this.keys.get(index);
    }

    public void insertKey(TrackKey key) {
        this.keys.add(key);
        Collections.sort(keys);
    }

    /*
     * For testing
     */
    public void printKeys() {
        for (TrackKey key: keys)
            System.out.println(key);
    }

    public String toString() {
        return String.format("Track(keys=%d)", keys.size());
    }
}
