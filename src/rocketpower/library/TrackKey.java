package rocketpower.library;


public class TrackKey implements Comparable<TrackKey> {
    public enum KeyType {
        STEP,
        LINEAR,
        SMOOTH,
        RAMP
    }

    private int row;
    private float value;
    private KeyType keyType;

    public TrackKey(int row, float value, KeyType keyType) {
        this.row = row;
        this.value = value;
        this.keyType = keyType;
    }

    public int compareTo(TrackKey other) {
        return row > other.row ? 1 : row < other.row ? -1 : 0;
    }
}
