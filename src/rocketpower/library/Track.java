package rocketpower.library;

import java.util.List;
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

    public List<TrackKey> getKeys() {
        return Collections.unmodifiableList(keys);
    }

    /*
     * Find key for the given row.
     *
     * @returns Key if found, otherwise null.
     */
    public TrackKey getKey(int row) {
        // TODO: better way to do this search
        int index = Collections.binarySearch(this.keys, new TrackKey(row, 0.f, TrackKey.KeyType.STEP));
        // Exact hit or "insertion point"; get key below that
        if (index >= 0) {
            return this.keys.get(index);
        } else {
            return this.keys.get(-index-2);
        }
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
