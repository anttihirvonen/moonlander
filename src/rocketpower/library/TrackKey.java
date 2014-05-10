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

    public int getRow() {
        return row;
    }

    public float getValue() {
        return value;
    }

    public KeyType getKeyType() {
        return keyType;
    }

    public int compareTo(TrackKey other) {
        return row > other.row ? 1 : row < other.row ? -1 : 0;
    }

    public String toString() {
        return String.format("TrackKey(row=%d value=%f type=%s)", row, value, keyType);
    }
}
